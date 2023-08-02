package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Reglement;
import com.siewe.inventorymanagementsystem.model.Vente;
import com.siewe.inventorymanagementsystem.model.enumeration.TypePaiement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VenteDto {

    private Long id;
    private String createdDate;
    private String numVente;
    private Long storeId;
    private Long customerId;
    private CustomerDto customer;
    private Double prixTotal;
    private Double acompte;
    private Double reste;
    private Double newReglement;
    private Long userId;
    private String userName;
    private List<OrderedProductDto> orderedProducts;
    private List<ReglementDto> reglements;
    private String typePaiement;
    private String typePaimentBadge;
    private Boolean deleted;

    /**
     * la somme d'argeent recu
     */
    private Double reglement;
    /**
     * le montant rendue
     */
    private Double rendu;

    public VenteDto createDTO(Vente vente) {
        VenteDto venteDto = new VenteDto();
        if(vente != null){
            venteDto.setId(vente.getId());
//            venteDto.setNumVente("Ref NO:"+ StringUtils.leftPad(vente.getId().toString(), 6, "0"));
            venteDto.setCreatedDate(vente.getCreatedDate());
            venteDto.setDeleted(vente.getDeleted());


            if(vente.getCustomer() != null){
                venteDto.setCustomerId(vente.getCustomer().getCustomerId());
                venteDto.setCustomer(new CustomerDto()
                        .createDTO(vente.getCustomer()));
            }

            if(vente.getUser() != null){
                venteDto.setUserId(vente.getUser().getUserId());
                venteDto.setUserName(vente.getUser().getName());
            }

            ArrayList<OrderedProductDto> orderedProductDtos = new ArrayList<>();
            if(vente.getOrderedProducts() != null){
                for(OrderedProduct orderedProduct: vente.getOrderedProducts()){
                    orderedProductDtos.add(new OrderedProductDto().createDTO(orderedProduct));
                }
            }
            venteDto.setOrderedProducts(orderedProductDtos);
            venteDto.setPrixTotal(vente.getPrixTotal());
            venteDto.setReglement(vente.getReglement());
            if(vente.getReglement() != null)
                venteDto.setRendu(vente.getPrixTotal() - vente.getReglement());

            venteDto.setTypePaiement(String.valueOf(vente.getTypePaiement()));
            venteDto.typePaimentBadge = "success";
            if(vente.getTypePaiement().equals(TypePaiement.Credit))
                venteDto.typePaimentBadge = "warning";

            double acompte = 0;
            ArrayList<ReglementDto> reglementDtos = new ArrayList<>();
            if(vente.getReglements() != null){
                for(Reglement reglement: vente.getReglements()){
                    reglementDtos.add(new ReglementDto().createDTO(reglement));
                    acompte += reglement.getAmount();
                }
            }
            venteDto.setReglements(reglementDtos);
            venteDto.setAcompte(acompte);
            venteDto.setReste(vente.getPrixTotal() - acompte);
            venteDto.setNewReglement(0.0);

            return venteDto;
        }
        return null;
    }
}
