package fr.td.inscription;

/**
 * Identité d'un grimpeur, créée à l'inscription.
 * C'est le seul lien entre les différents modèles du grimpeur
 * (fr.td.inscription.Grimpeur, fr.td.reservation.Grimpeur) : ils partagent
 * un identifiant, pas un objet.
 */
public record GrimpeurId(String valeur) {
    public GrimpeurId {
        if (valeur == null || valeur.isBlank())
            throw new IllegalArgumentException("l'identifiant du grimpeur est obligatoire");
    }
}
