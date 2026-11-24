package fr.td.domaine;

public record Entrees(int nombre) {
    public Entrees {
        if (nombre < 0)
            throw new IllegalArgumentException("le nombre d'entrees ne peut pas etre negatif");
    }
}
