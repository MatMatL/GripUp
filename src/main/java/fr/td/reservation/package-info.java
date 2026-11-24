/**
 * Contexte « Réservation et fréquentation » : le CORE de GripUp.
 *
 * Réserver un créneau, annuler, enregistrer les passages, constater absences
 * et annulations tardives (G2 à G9). Ne vend rien et ne gère pas les cours.
 *
 * Langage : créneau, réservation, passage, défection, complet.
 * Les commandes renvoient un événement au passé (CreneauReserve,
 * ReservationAnnulee) que les autres contextes peuvent écouter.
 */
package fr.td.reservation;
