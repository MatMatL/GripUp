package fr.td.formules;

/**
 * Ce que notre modèle sait d'un paiement, une fois traduit par l'ACL PaySûr.
 * Deux cas seulement : accepté ou refusé. Aucun code PaySûr ici.
 */
public sealed interface ResultatDePaiement {

    String referenceDuPaiement();

    record PaiementAccepte(String referenceDuPaiement, Montant montant) implements ResultatDePaiement {
        public PaiementAccepte {
            if (referenceDuPaiement == null || referenceDuPaiement.isBlank())
                throw new IllegalArgumentException("la reference du paiement est obligatoire");
            if (montant == null)
                throw new IllegalArgumentException("le montant est obligatoire");
        }
    }

    record PaiementRefuse(String referenceDuPaiement, MotifDeRefus motif) implements ResultatDePaiement {
        public PaiementRefuse {
            if (referenceDuPaiement == null || referenceDuPaiement.isBlank())
                throw new IllegalArgumentException("la reference du paiement est obligatoire");
            if (motif == null)
                throw new IllegalArgumentException("le motif du refus est obligatoire");
        }
    }
}
