package fr.td.formules;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Une somme en euros, jamais négative, au centime près. */
public record Montant(BigDecimal euros) {
    public Montant {
        if (euros == null)
            throw new IllegalArgumentException("le montant est obligatoire");
        if (euros.signum() < 0)
            throw new IllegalArgumentException("un montant ne peut pas etre negatif");
        if (euros.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("un montant s'exprime au centime pres");
        euros = euros.setScale(2, RoundingMode.UNNECESSARY);
    }

    public static Montant euros(String valeur) {
        return new Montant(new BigDecimal(valeur));
    }
}
