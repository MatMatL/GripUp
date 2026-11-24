package fr.td.formules.paysur;

/**
 * La réponse de PaySûr, telle qu'il l'envoie, sans interprétation :
 * un code texte (« PAID », « FAILED_NSF »…), un montant en centimes et sa
 * référence d'opération. Cette classe ne sort jamais du package de l'ACL
 * vers le modèle.
 */
public record ReponsePaySur(String status, long amountInCents, String transactionId) {
}
