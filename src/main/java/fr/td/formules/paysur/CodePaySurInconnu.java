package fr.td.formules.paysur;

/**
 * PaySûr a renvoyé un code que l'ACL ne sait pas traduire.
 * On refuse de deviner : ni succès ni échec par défaut. L'opération est mise
 * de côté pour être traitée à la main, et l'abonnement ne change pas d'état.
 */
public class CodePaySurInconnu extends RuntimeException {
    public CodePaySurInconnu(String code, String transactionId) {
        super("code PaySûr inconnu « " + code + " » pour l'opération " + transactionId);
    }
}
