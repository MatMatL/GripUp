package fr.td.reservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.td.inscription.GrimpeurId;

/**
 * Le grimpeur vu par la Réservation : ses réservations de créneau à venir,
 * et ce que Formules et abonnements lui a dit de sa formule (abonné ou
 * porteur de carte, suspendu ou non).
 *
 * Il ne connaît ni son nom ni sa date de naissance : voir fr.td.inscription.Grimpeur.
 */
public class Grimpeur {
    public enum Formule {
        ABONNE,
        PORTEUR_DE_CARTE
    }

    /** G4 */
    public static final int RESERVATIONS_A_VENIR_MAXIMUM = 2;
    /** G3 */
    public static final long HEURES_D_AVANCE_ABONNE = 7 * 24;
    /** G3 */
    public static final long HEURES_D_AVANCE_PORTEUR_DE_CARTE = 48;
    /** G8 */
    public static final long HEURES_AVANT_ANNULATION_TARDIVE = 24;

    private final GrimpeurId id;
    private final Formule formule;
    private final Set<Creneau> reservations = new HashSet<>();
    private boolean abonnementSuspendu;

    public Grimpeur(GrimpeurId id, Formule formule) {
        if (id == null)
            throw new IllegalArgumentException("l'identifiant du grimpeur est obligatoire");
        if (formule == null)
            throw new IllegalArgumentException("la formule du grimpeur est obligatoire");
        this.id = id;
        this.formule = formule;
    }

    public GrimpeurId id() {
        return id;
    }

    public Set<Creneau> reservations() {
        return Collections.unmodifiableSet(reservations);
    }

    /** Commande « Réserver un créneau » : produit CreneauReserve, ou refuse sans rien changer. */
    public CreneauReserve reserver(Creneau creneau, LocalDateTime maintenant) {
        if (creneau == null)
            throw new IllegalArgumentException("le creneau est obligatoire");
        if (abonnementSuspendu && formule == Formule.ABONNE)
            throw new ReservationRefusee("G17 : l'abonnement suspendu ne peut pas reserver");
        if (creneau.estCommence(maintenant))
            throw new ReservationRefusee("on ne reserve pas un creneau deja commence");
        if (reservations.contains(creneau))
            throw new ReservationRefusee("ce creneau est deja reserve");

        long aVenir = reservations.stream()
                .filter(creneauReserve -> creneauReserve.estAVenir(maintenant))
                .count();
        if (aVenir >= RESERVATIONS_A_VENIR_MAXIMUM)
            throw new ReservationRefusee("G4 : un grimpeur ne peut pas avoir plus de 2 reservations de creneau");

        long heuresAVance = ChronoUnit.HOURS.between(maintenant, creneau.debut());
        if (formule == Formule.ABONNE && heuresAVance > HEURES_D_AVANCE_ABONNE)
            throw new ReservationRefusee("G3 : un abonne reserve au plus 7 jours a l'avance");
        if (formule == Formule.PORTEUR_DE_CARTE && heuresAVance > HEURES_D_AVANCE_PORTEUR_DE_CARTE)
            throw new ReservationRefusee("G3 : un porteur de carte reserve au plus 48 heures a l'avance");

        reservations.add(creneau);
        return new CreneauReserve(id, creneau);
    }

    /** Commande « Annuler une réservation » : produit ReservationAnnulee (tardive selon G8). */
    public ReservationAnnulee annuler(Creneau creneau, LocalDateTime maintenant) {
        if (creneau == null)
            throw new IllegalArgumentException("le creneau est obligatoire");
        if (!reservations.contains(creneau))
            throw new ReservationRefusee("ce creneau n'est pas reserve par ce grimpeur");
        if (creneau.estCommence(maintenant))
            throw new ReservationRefusee("on n'annule pas un creneau deja commence");

        boolean tardive = ChronoUnit.MINUTES.between(maintenant, creneau.debut())
                < HEURES_AVANT_ANNULATION_TARDIVE * 60;
        reservations.remove(creneau);
        return new ReservationAnnulee(id, creneau, tardive);
    }

    public void suspendreAbonnement() {
        if (formule == Formule.ABONNE)
            abonnementSuspendu = true;
    }

    public void regulariserAbonnement() {
        abonnementSuspendu = false;
    }

    public boolean abonnementEstSuspendu() {
        return abonnementSuspendu;
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre)
            return true;
        if (!(autre instanceof Grimpeur grimpeur))
            return false;
        return Objects.equals(id, grimpeur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
