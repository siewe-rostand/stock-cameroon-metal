package com.siewe.inventorymanagementsystem.controller;

import com.siewe.inventorymanagementsystem.repository.VenteRepository;
import com.siewe.inventorymanagementsystem.service.OneSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OneSignalController {
    @Autowired
    private OneSignalService oneSignalService;

    @Autowired
    private VenteRepository venteRepository;

//    @RequestMapping(method = RequestMethod.POST, value = "/notify-new-vente")
//    public String notifyNewVente(@RequestParam(name = "playerId") String playerId,
//                                 @RequestParam(name = "venteId") Long venteId)
//            throws NoSuchAlgorithmException, KeyManagementException {
//
//        Vente vente = venteRepository.findByVenteId(venteId);
//        if(vente != null){
//
//            StringBuilder s = new StringBuilder();
//            /*s.append(vente.getProduct().getName()).append(": ");
//            s.append(vente.getQuantity()).append(" x ").append( Math.round(vente.getPrixVente())).append(" = ");
//            s.append(Math.round(vente.getQuantity() * vente.getPrixVente()) ).append(" Fcfa");*/
//            s.append(vente.getPrixTotal()).append(" Fcfa");
//
//            //System.out.println(s.toString());
//            return oneSignalService.createUserNotification(playerId, "Nouvelle vente !", s.toString());
//        }
//        return null;
//    }
}

