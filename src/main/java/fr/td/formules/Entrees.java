package fr.td.formules;

/** Nombre d'entrées d'une carte (G1, G6). */
public record Entrees(int nombre) {
    public Entrees {
        if (nombre < 0)
            throw new IllegalArgumentException("le nombre d'entrees ne peut pas etre negatif");
    }
}
