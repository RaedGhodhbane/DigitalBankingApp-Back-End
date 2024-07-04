package tn.banque.springbootmvc.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.banque.springbootmvc.enums.AccountStatus;

import java.util.Date;
import java.util.List;
// BankAccount est une entité ( classe ) JPA possède Getters & setters, constructeurs, ses propres
// méthodes d'objets c'est une classe Java (JPA) alors que BankAccountDTO est un modèle ( partie )
// de la classe Java (JPA)
@Entity
// @Entity pour mappage hibernate la classe BankAccount devient une table
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
/*
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
on a choisi la stratégie single table le mappage hibernate va créer une seule table BankAccount
avec une colonne spéciale qui désigne le type du compte
*/

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// la colonne spéciale nommée TYPE length = TAILLE EN CARACTERE
// Stratégie SINGLE_TABLE
@DiscriminatorColumn(name="TYPE", length = 4)
/* Les annotations relatives à la dépendance lombok sont :
@Data sert à : Créer les structures de la classe (les attributs type primary key foreign key , getters , setters , toString
@NoArgsConstructor sert à : Créer le constructeur par défaut
@AllArgsConstructor sert à : Créer le constructeur paramétré
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public  class BankAccount {
    @Id // @Id pour le mappage  de la clé primaire
    private String id;
    private double balance;
    private Date createdAt;
    // mappage pour les énumérations
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    /* mappage hibernate relationnel qui exprime l'association (Many BankAccount/Customer One) un customer peut avoir plusieurs
       comptes bancaires
     */
    @ManyToOne
    private Customer customer;
    /* mappage hibernate relationnel qui exprime l'association (One BankAccount/accountOperation Many) un compte peut avoir plusieurs
       operations bancaires
     */
    //Important
    @OneToMany(mappedBy = "bankAccount" , fetch = FetchType.LAZY)
    /* cette annotation @JsonProperty est utilisée pour ne pas afficher
       la liste des opérations bancaires
    */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@JsonProperty est utilisée pour afficher les attributs seulement sans le mappage entre les tables
    // On peut utiliser soit @JsonProperty ou bien faire un modèle DTO ( Data To Object ) pour le JPAEntity
    private List<AccountOperation> accountOperation;

}
