package fr.td.domaine;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Grimpeur {
    public enum Formule {
        ABONNE,
        PORTEUR_DE_CARTE
    }

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

    public void reserver(Creneau creneau, LocalDateTime maintenant) {
        if (creneau == null)
            throw new IllegalArgumentException("le creneau est obligatoire");
        if (abonnementSuspendu && formule == Formule.ABONNE)
            throw new DomaineException("G17 : l'abonnement suspendu ne peut pas reserver");

        long aVenir = reservations.stream()
                .filter(creneauReserve -> creneauReserve.estAVenir(maintenant))
                .count();
        if (aVenir >= 2)
            throw new DomaineException("G4 : un grimpeur ne peut pas avoir plus de 2 reservations de creneau");

        long heuresAVance = ChronoUnit.HOURS.between(maintenant, creneau.debut());
        if (formule == Formule.ABONNE && heuresAVance > 7 * 24)
            throw new DomaineException("G3 : un abonne reserve au plus 7 jours a l'avance");
        if (formule == Formule.PORTEUR_DE_CARTE && heuresAVance > 48)
            throw new DomaineException("G3 : un porteur de carte reserve au plus 48 heures a l'avance");

        reservations.add(creneau);
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
