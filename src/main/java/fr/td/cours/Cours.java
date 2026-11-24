package fr.td.cours;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import fr.td.inscription.GrimpeurId;

public class Cours {
    /** G10 */
    public static final int CAPACITE_MAXIMALE = 8;

    private final CoursId id;
    private final Set<GrimpeurId> participants = new LinkedHashSet<>();

    public Cours(CoursId id) {
        if (id == null)
            throw new IllegalArgumentException("l'identifiant du cours est obligatoire");
        this.id = id;
    }

    public CoursId id() {
        return id;
    }

    public Set<GrimpeurId> participants() {
        return Collections.unmodifiableSet(participants);
    }

    public boolean estComplet() {
        return participants.size() >= CAPACITE_MAXIMALE;
    }

    public void inscrire(GrimpeurId grimpeur) {
        if (grimpeur == null)
            throw new IllegalArgumentException("le grimpeur est obligatoire");
        if (estComplet())
            throw new InscriptionAuCoursRefusee("G10 : un cours collectif accueille au plus 8 grimpeurs");
        if (!participants.add(grimpeur))
            throw new InscriptionAuCoursRefusee("ce grimpeur est deja inscrit au cours");
    }

    public void desinscrire(GrimpeurId grimpeur) {
        if (grimpeur != null)
            participants.remove(grimpeur);
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre)
            return true;
        if (!(autre instanceof Cours cours))
            return false;
        return Objects.equals(id, cours.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
