/**
 * Contexte « Formules et abonnements » (supporting).
 *
 * Vendre une formule, l'activer au paiement, la suspendre (G17) ou l'expirer
 * (G18), décompter les entrées (G6). Ne fait ni le paiement (PaySûr) ni la
 * comptabilité.
 *
 * Ce package ne parle QUE le langage du glossaire : Montant, ResultatDePaiement,
 * MotifDeRefus. Les codes et les centimes de PaySûr restent dans
 * fr.td.formules.paysur (l'ACL).
 */
package fr.td.formules;
