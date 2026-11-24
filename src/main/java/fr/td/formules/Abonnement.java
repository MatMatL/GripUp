package fr.td.formules;

import java.util.Objects;

import fr.td.inscription.GrimpeurId;

/**
 * L'abonnement mensuel d'un abonné, du point de vue des Formules.
 * Il réagit au résultat d'un prélèvement, déjà traduit par l'ACL :
 * il ne voit jamais « PAID » ni « FAILED_NSF ».
 */
public class Abonnement {
    private final GrimpeurId abonne;
    private boolean suspendu;

    public Abonnement(GrimpeurId abonne) {
        if (abonne == null)
            throw new IllegalArgumentException("l'abonne est obligatoire");
        this.abonne = abonne;
    }

    public GrimpeurId abonne() {
        return abonne;
    }

    public boolean estSuspendu() {
        return suspendu;
    }

    /** G17 : un prélèvement refusé suspend l'abonnement ; un paiement accepté le régularise. */
    public void prendreEnCompte(ResultatDePaiement prelevement) {
        if (prelevement == null)
            throw new IllegalArgumentException("le resultat du prelevement est obligatoire");
        suspendu = prelevement instanceof ResultatDePaiement.PaiementRefuse;
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre)
            return true;
        if (!(autre instanceof Abonnement abonnement))
            return false;
        return Objects.equals(abonne, abonnement.abonne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abonne);
    }
}
