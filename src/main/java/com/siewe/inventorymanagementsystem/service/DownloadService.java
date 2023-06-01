package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Vente;
import com.siewe.inventorymanagementsystem.repository.VenteRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.apache.tomcat.util.codec.binary.StringUtils.getBytesUtf8;
import static org.apache.tomcat.util.codec.binary.StringUtils.newStringUtf8;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Value("${dir.pharma}")
    private String FOLDER;

    @Value("${store.name}")
    private String STORE_NAME;

    @Value("${store.address}")
    private String STORE_ADDRESS;

    @Value("${store.phone}")
    private String STORE_PHONE;

    @Autowired
    private VenteRepository venteRepository;


    static class AfficheurFlux implements Runnable {

        private final InputStream inputStream;
        private String output;

        AfficheurFlux(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        private BufferedReader getBufferedReader(InputStream is) {
            return new BufferedReader(new InputStreamReader(is));
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        @Override
        public void run() {
            BufferedReader br = getBufferedReader(inputStream);
            String ligne = "";
            this.output = "";
            try {
                while ((ligne = br.readLine()) != null) {
                    //System.out.println(ligne);
                    this.output += ligne;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String createInvoice(Long venteId) {
        Vente vente = venteRepository.findOne(venteId);

        String filename = "invoice";

        String TEX_FOLDER = FOLDER + "tex/";
        //String makefile = createMakeFile(filename);
        String makefile = createMakeFile();
        String texTemplate = createInvoiceTex(vente);

        try {
            byte[] bytes = getBytesUtf8(texTemplate);
            String Utf8EncodedString = newStringUtf8(bytes);

            /*FileUtils.writeStringToFile(new File(TEX_FOLDER + "generate.bat"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER + filename+".tex"), Utf8EncodedString, "UTF-8");

            String[] paramsArr = {"cmd.exe", "/c", "generate.bat"};
            List<String> paramsList = Arrays.asList(paramsArr);*/

            FileUtils.writeStringToFile(new File(TEX_FOLDER + "Makefile"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER + "invoice.tex"), texTemplate);

            String[] paramsArr = {"make"};
            List<String> paramsList = Arrays.asList(paramsArr);

            ProcessBuilder pb = new ProcessBuilder(paramsList);
            pb.directory(new File(TEX_FOLDER));

            try {
                Process p = pb.start();
                AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
                AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
                new Thread(fluxSortie).start();
                new Thread(fluxErreur).start();
                p.waitFor();

                return TEX_FOLDER + filename+".pdf";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private String createMakeFile() {
        StringBuilder s = new StringBuilder();
        return s.append("\tpdflatex -synctex=1 -interaction=nonstopmode %.tex\n").toString();
        *//* s
            .append("all :\n")
            .append("\trm -rf *.log *.aux *.pdf\n")
            //to time for watermark to render correctly
            .append("\tpdflatex *.tex\n" )
            .append("\tpdflatex *.tex\n" )
            .append("\trm -rf *.log *.aux *.tex *.epl");*//*
    }*/

    private String createMakeFile() {
        StringBuilder s = new StringBuilder();
        s.append("all :\n")
                .append("\trm -rf *.log *.aux *.pdf\n")
                //two time for watermark to render correctly
                .append("\tpdflatex *.tex\n" )
                .append("\tpdflatex *.tex\n" )
                .append("\trm -rf *.log *.aux *.tex *.epl");
        return s.toString();
    }

    /*private String createMakeFile(String filename) {
        return "\t@ECHO OFF\n"+
                "\tpdflatex "+filename+".tex\n" +
                "\tpdflatex "+filename+".tex\n" +
                "\tdel /F *.log *.aux *.tex generate.bat\n";
    }*/

    private String createInvoiceTex2(Vente vente) {
        StringBuilder s = new StringBuilder();
        s.append("\\documentclass[a4paper,9pt]{memoir}\n" +
                "\\usepackage[frenchb]{babel}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\n" +
                "\\setstocksize{80mm}{50mm} % UK Stock size\n" +
                "\\setpagecc{80mm}{50mm}{*}\n" +
                "\\settypeblocksize{76mm}{46mm}{*}\n" +
                "\\setulmargins{2mm}{*}{*}\n" +
                "\\setlrmargins{2mm}{*}{*}\n" +
                "\n" +
                "\\def \\tab {\\hspace*{2ex}} \n" +
                "\\setlength{\\parindent}{0pt}\n" +
                "\\setlength{\\parskip}{1ex plus 0.5ex minus 0.2ex}\n" +
                "\\pagenumbering{gobble}\n" +
                "\n" +
                "\\setheadfoot{0.05pt}{0.05pt}\n" +
                "\\setheaderspaces{1pt}{*}{*}\n" +
                "\\checkandfixthelayout\n" +
                "\n" +
                "\\begin{document}\n" +
                "\n" +
                "\\tiny\n" +
                "\\begin{center}\n" +
                "\\textbf{"+STORE_NAME+"}\\\\\n"
                + STORE_ADDRESS+"\\\\\n" +
                "T.E.L. "+ STORE_PHONE +"\\\\\n" +
                "\\end{center}\n" +
                "\n" +
                "\\hrule\n" +
                "\n" +
                "~\\\\\n" +
                "Vous avez été reçu par \\uppercase{" + vente.getUser().getName() + "}\n" +
                "~\\\\\n" +
                "Le \\tab "+ vente.getDate() + "\n" +
                "~\\\\\n" +
                "Numéro de la vente : "+  StringUtils.leftPad(vente.getId().toString() , 6, "0") + " \n" +
                "\n" +
                "~\\\\\n" +
                "\\hrule % Horizontal line\n" +
                "\\uppercase{Détail de la commande}\\\\ \\\\\n" );
                /*for(OrderedProduct op: vente.getOrderedProducts()){
                   String name =  op.getProduct().getName().replace("%","\\%");
                    s.append(Math.round(op.getQuantity()) + "x").append("\t")
                            .append(name).append("\\hfill ")
                            .append("\\textbf{"+ (Math.round(op.getQuantity() * op.getPrixVente()))+"}")
                            .append("\\\\\n");
                    s.append("\\\\");
                }*/
//        for(OrderedProduct op: vente.getOrderedProducts()){
//            String name =  op.getProduct().getName().replace("%","\\%");
//            s.append("\\begin{minipage}[t]{0.1\\linewidth}\n" +
//                    Math.round(op.getQuantity()) + "x" + "\n" +
//                    "\\end{minipage}\n" +
//                    "\\begin{minipage}[t]{0.70\\linewidth}\n" +
//                    name + "\n" +
//                    "\\end{minipage}\n" +
//                    "\\begin{minipage}[t]{0.2\\linewidth}\n" +
//                    "\\hfill \\textbf{"+(Math.round(op.getQuantity() * op.getUnitPrice()))+"}\n" +
//                    "\\end{minipage}");
//            s.append("\\\\");
//        }
        s.append(
                "  \n" +
                        "~\\\\\n" +
                        "\\hrule\n" +
                        "\\vspace{0.2cm}\n" +
                        "\\centering{Total  : \\large{\\textbf{ " + Math.round(vente.getPrixTotal())+"}} \\tiny{FCFA}}\n" +
                        "\n" +
                        "\\vfill\n" +
                        "\\hrule % Horizontal line\n" +
                        "\\centering{\\uppercase{Merçi et à bientôt !}}\n" +
                        "\n" +
                        "\\end{document}"
        );
        return s.toString();
    }

    private String createInvoiceTex(Vente vente) {
        StringBuilder s = new StringBuilder();
        s.append("\\documentclass[a4paper,french,10pt]{report}\n" +
                "\\usepackage{babel}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[a4paper]{geometry}\n" +
                "\\usepackage{fancyhdr}\n" +
                //"\\usepackage{bera}\n" +
                "\n" +
                "\n" +
                "\\def \\tab {\\hspace*{2ex}} % Define \\tab to create some horizontal white space\n" +
                "\\geometry{verbose,tmargin=4em,bmargin=4em,lmargin=4em,rmargin=4em}\n" +
                "\n" +
                "\n" +
                "\\def \\tab {\\hspace*{2ex}} \n" +
                "\\setlength{\\parindent}{0pt}\n" +
                "\\setlength{\\parskip}{1ex plus 0.5ex minus 0.2ex}\n" +
                "\\pagenumbering{gobble}\n" +
                "\n" +
                "\n" +
                "\\begin{document}\n" +
                "\\bfseries\n" +
                "\\huge\n" +
                "\\begin{center}\n" +
                "\\textbf{"+STORE_NAME+"}\n" +
                "\\vspace{0.2cm}\\\\\n" +
                STORE_ADDRESS +"\n" +
                "\\vspace{0.2cm}\\\\\n" +
                "T.E.L. "+ STORE_PHONE +"\n" +
                "\\end{center}\n" +
                "\n" +
                "\\vspace{0.5cm}\n" +
                "\\hrule\n" +
                "\\vspace{1cm}\n" +
                "\n" +
                "Vous avez été reçu par \\uppercase{" + vente.getUser().getName() + "}\n" +
                "\\vspace{0.2cm}\\\\\n" +
                "Le \\tab "+ vente.getDate() + "\n" +
                "\\vspace{0.2cm}\\\\\n" +
                "Numéro de la vente : "+  StringUtils.leftPad(vente.getId().toString() , 6, "0") + " \n" +
                "\n" +
                "\\vspace{0.5cm}\n" +
                "\\hrule\n" +
                "\\vspace{1cm}\n" +
                "\n" +
                "\\uppercase{Détail de la commande}\\\\ \n" +
                "\n");

//        for(OrderedProduct op: vente.getOrderedProducts()){
//            String name =  op.getProduct().getName().replace("%","\\%");
//            s.append("\\begin{minipage}[t]{0.1\\linewidth}\n" +
//                    Math.round(op.getQuantity()) + "x" + "\n" +
//                    "\\end{minipage}\n" +
//                    "\\begin{minipage}[t]{0.70\\linewidth}\n" +
//                    name + "\n" +
//                    "\\end{minipage}\n" +
//                    "\\begin{minipage}[t]{0.2\\linewidth}\n" +
//                    "\\hfill \\textbf{"+(Math.round(op.getQuantity() * op.getUnitPrice()))+"}\n" +
//                    "\\end{minipage}" +
//                    "\n" );
//        }
        s.append(
                "\\vspace{1cm}\n" +
                        "\\hrule\n" +
                        "\\vspace{1cm}\n" +
                        "\n" +
                        "\\centering{TOTAL  : \\Huge{\\textbf{"+ Math.round(vente.getPrixTotal())+"}} \\huge{FCFA}}\n" +
                        "\n" +
                        "\\vfill\n" +
                        "\\hrule % Horizontal line\n" +
                        "\\centering{\\uppercase{Merçi et à bientôt !}}\n" +
                        "\n" +
                        "\\end{document}"
        );
        return s.toString();
    }
}
