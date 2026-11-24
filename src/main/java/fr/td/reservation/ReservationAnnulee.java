package fr.td.reservation;

import fr.td.inscription.GrimpeurId;

/**
 * Événement : un grimpeur a annulé une réservation de créneau.
 * tardive = annulée moins de 24 h avant le début (G8) : Formules et abonnements
 * en déduit les entrées perdues du porteur de carte, et le compteur de
 * défections (G9) en tient compte.
 */
public record ReservationAnnulee(GrimpeurId grimpeur, Creneau creneau, boolean tardive) {
}
