package tn.banque.springbootmvc.dtos;

import lombok.Data;

import java.util.List;

// DTO c'est un modéle de l'entité AccountHistory
// Pour afficher juste les parties qui seront affiché dans la partie front
// C'est à dire éliminer l'affichage des données de l'entité relié avec la classe mère
// Exemple : Customer qui a une relation avec -> BankAccount : DTO pour éliminer
// toutes sortes de relation entre les tables et afficher juste les données qu'on veut
// afficher de la table

@Data // pour les getters et les setters
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
