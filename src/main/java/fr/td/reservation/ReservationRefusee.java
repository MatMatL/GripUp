package fr.td.reservation;

/** Levée quand une règle du contexte Réservation refuse la commande. */
public class ReservationRefusee extends RuntimeException {
    public ReservationRefusee(String message) {
        super(message);
    }
}
