package fr.td.formules.paysur;

import java.math.BigDecimal;

import fr.td.formules.Montant;
import fr.td.formules.MotifDeRefus;
import fr.td.formules.ResultatDePaiement;
import fr.td.formules.ResultatDePaiement.PaiementAccepte;
import fr.td.formules.ResultatDePaiement.PaiementRefuse;

/**
 * Le seul endroit qui connaît les codes et les unités de PaySûr.
 * Entrée : ReponsePaySur (leurs mots). Sortie : ResultatDePaiement (nos mots).
 */
public class TraducteurPaySur {
    private static final String PAYE = "PAID";
    private static final String PROVISION_INSUFFISANTE = "FAILED_NSF";

    public ResultatDePaiement traduire(ReponsePaySur reponse) {
        if (reponse == null)
            throw new IllegalArgumentException("la reponse PaySûr est obligatoire");
        String code = reponse.status() == null ? "" : reponse.status().trim();
        return switch (code) {
            case PAYE -> new PaiementAccepte(reponse.transactionId(), enEuros(reponse.amountInCents()));
            case PROVISION_INSUFFISANTE -> new PaiementRefuse(reponse.transactionId(), MotifDeRefus.PROVISION_INSUFFISANTE);
            default -> throw new CodePaySurInconnu(reponse.status(), reponse.transactionId());
        };
    }

    private static Montant enEuros(long centimes) {
        return new Montant(BigDecimal.valueOf(centimes, 2));
    }
}
