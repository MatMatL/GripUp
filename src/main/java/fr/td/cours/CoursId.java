package fr.td.cours;

public record CoursId(String valeur) {
    public CoursId {
        if (valeur == null || valeur.isBlank())
            throw new IllegalArgumentException("l'identifiant du cours est obligatoire");
    }
}
