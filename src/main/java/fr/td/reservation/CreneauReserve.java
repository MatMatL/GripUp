package fr.td.reservation;

import fr.td.inscription.GrimpeurId;

/**
 * Événement : un grimpeur a réservé un créneau (G3, G4, G17 respectées).
 * Déclenche, par exemple, le SMS de confirmation (précisions : « Chaque
 * réservation est confirmée par SMS »).
 */
public record CreneauReserve(GrimpeurId grimpeur, Creneau creneau) {
}
