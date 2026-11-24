package fr.td.domaine;

public record GrimpeurId(String valeur) {
    public GrimpeurId {
        if (valeur == null || valeur.isBlank())
            throw new IllegalArgumentException("l'identifiant du grimpeur est obligatoire");
    }
}
