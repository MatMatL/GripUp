package fr.td.inscription;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

/**
 * Le grimpeur vu par l'accueil : une personne inscrite, avec son nom,
 * sa date de naissance et, pour un mineur, l'adulte qui l'accompagne.
 *
 * Il ne connaît ni ses réservations ni sa formule : ce sont d'autres contextes.
 * Même mot, autre modèle : voir fr.td.reservation.Grimpeur.
 */
public class Grimpeur {
    /** G15 : en dessous de cet âge, le grimpeur doit être accompagné. */
    public static final int AGE_SANS_ACCOMPAGNATEUR = 14;

    private final GrimpeurId id;
    private final String nom;
    private final LocalDate dateDeNaissance;
    private final GrimpeurId accompagnateur;

    private Grimpeur(GrimpeurId id, String nom, LocalDate dateDeNaissance, GrimpeurId accompagnateur) {
        if (id == null)
            throw new IllegalArgumentException("l'identifiant du grimpeur est obligatoire");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("le nom du grimpeur est obligatoire");
        if (dateDeNaissance == null)
            throw new IllegalArgumentException("la date de naissance est obligatoire");
        this.id = id;
        this.nom = nom;
        this.dateDeNaissance = dateDeNaissance;
        this.accompagnateur = accompagnateur;
    }

    /** Inscription d'un grimpeur de 14 ans ou plus. */
    public static Grimpeur inscrire(GrimpeurId id, String nom, LocalDate dateDeNaissance, LocalDate aujourdhui) {
        Grimpeur grimpeur = new Grimpeur(id, nom, dateDeNaissance, null);
        if (grimpeur.estMineur(aujourdhui))
            throw new InscriptionRefusee("G15 : un grimpeur de moins de 14 ans s'inscrit avec un accompagnateur");
        return grimpeur;
    }

    /** Inscription d'un mineur, rattaché à un adulte déjà inscrit (G15). */
    public static Grimpeur inscrireMineur(GrimpeurId id, String nom, LocalDate dateDeNaissance,
                                         Grimpeur accompagnateur, LocalDate aujourdhui) {
        if (accompagnateur == null)
            throw new InscriptionRefusee("G15 : un mineur s'inscrit avec un accompagnateur");
        if (accompagnateur.estMineur(aujourdhui))
            throw new InscriptionRefusee("G15 : l'accompagnateur doit être un adulte inscrit");
        return new Grimpeur(id, nom, dateDeNaissance, accompagnateur.id());
    }

    public GrimpeurId id() {
        return id;
    }

    public String nom() {
        return nom;
    }

    public boolean estMineur(LocalDate aujourdhui) {
        return Period.between(dateDeNaissance, aujourdhui).getYears() < AGE_SANS_ACCOMPAGNATEUR;
    }

    public Optional<GrimpeurId> accompagnateur() {
        return Optional.ofNullable(accompagnateur);
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
