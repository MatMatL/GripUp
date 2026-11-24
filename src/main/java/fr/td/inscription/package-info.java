/**
 * Contexte « Inscription et badges » (generic).
 *
 * Inscrit un grimpeur une fois (nom, date de naissance, téléphone, email),
 * rattache l'accompagnateur d'un mineur (G15). Ne décide ni des droits
 * d'accès ni des réservations.
 *
 * Ce contexte crée l'identité du grimpeur : {@link fr.td.inscription.GrimpeurId}
 * est publiée aux contextes en aval (Customer/Supplier), qui ne partagent
 * avec lui que cet identifiant, jamais l'objet Grimpeur.
 */
package fr.td.inscription;
