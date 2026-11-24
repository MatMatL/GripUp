package fr.td;

import static fr.td.Verificateur.*;

import java.time.LocalDateTime;

import fr.td.domaine.Cours;
import fr.td.domaine.CoursId;
import fr.td.domaine.Creneau;
import fr.td.domaine.DomaineException;
import fr.td.domaine.Entrees;
import fr.td.domaine.Grimpeur;
import fr.td.domaine.GrimpeurId;

// Vos classes du package fr.td.domaine s'importent ici (IntelliJ le fait pour vous : Alt+Entrée).

/**
 * VOS vérifications. Lancez ce programme (clic droit > Run 'Verifications.main()').
 *
 * Une vérification par règle, au minimum. Chacune montre que la règle est
 * respectée... et qu'on ne peut pas la contourner.
 */
public class Verifications {

    public static void main(String[] args) {
        titre("Value objects");
        verifier("G2", "un créneau refuse une heure qui n'est pas fixe",
                () -> refuse(() -> new Creneau(LocalDateTime.of(2026, 3, 1, 11, 0))));
        verifier("VO", "Entrees refuse un nombre negatif",
                () -> refuse(() -> new Entrees(-1)));
        verifier("VO", "deux identifiants de grimpeur vides sont refusés",
                () -> refuse(() -> new GrimpeurId("")));

        titre("Grimpeur (entité)");
        verifier("G4", "un grimpeur ne peut pas avoir plus de 2 reservations",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 10, 20, 0)),
                            LocalDateTime.of(2026, 3, 1, 10, 0)));
                });
        verifier("G17", "un abonne suspendu ne peut plus reserver",
                () -> {
                    Grimpeur grimpeur = grimpeur("G2", Grimpeur.Formule.ABONNE);
                    grimpeur.suspendreAbonnement();
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 2, 14, 0)),
                            LocalDateTime.of(2026, 3, 1, 10, 0)));
                });
        verifier("G17", "la regularisation rend de nouveau possible la reservation",
                () -> {
                    Grimpeur grimpeur = grimpeur("G3", Grimpeur.Formule.ABONNE);
                    grimpeur.suspendreAbonnement();
                    grimpeur.regulariserAbonnement();
                    grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 2, 14, 0)),
                            LocalDateTime.of(2026, 3, 1, 10, 0));
                    attendu(1, grimpeur.reservations().size());
                });

        titre("Cours (entité)");
        verifier("G10", "un cours complet refuse un neuvieme grimpeur",
                () -> {
                    Cours cours = coursAvecHuitGrimpeurs();
                    Grimpeur neuvieme = grimpeur("G9", Grimpeur.Formule.ABONNE);
                    refuse(() -> cours.inscrire(neuvieme));
                });
        verifier("G10", "une place liberee peut etre redonnée",
                () -> {
                    Cours cours = coursAvecHuitGrimpeurs();
                    Grimpeur premier = cours.participants().iterator().next();
                    cours.desinscrire(premier);
                    cours.inscrire(grimpeur("G10", Grimpeur.Formule.PORTEUR_DE_CARTE));
                    attendu(8, cours.participants().size());
                });

        bilan();
    }

    // ---------------------------------------------------------------------
    private static Grimpeur grimpeur(String id, Grimpeur.Formule formule) {
        return new Grimpeur(new GrimpeurId(id), formule);
    }

    private static Grimpeur abonneAvecDeuxReservations() {
        Grimpeur grimpeur = grimpeur("G1", Grimpeur.Formule.ABONNE);
        LocalDateTime maintenant = LocalDateTime.of(2026, 3, 1, 10, 0);
        grimpeur.reserver(new Creneau(LocalDateTime.of(2026, 3, 2, 10, 0)), maintenant);
        grimpeur.reserver(new Creneau(LocalDateTime.of(2026, 3, 3, 12, 0)), maintenant);
        return grimpeur;
    }

    private static Cours coursAvecHuitGrimpeurs() {
        Cours cours = new Cours(new CoursId("C1"));
        for (int i = 1; i <= 8; i++)
            cours.inscrire(grimpeur("G" + i, Grimpeur.Formule.ABONNE));
        return cours;
    }
    // ---------------------------------------------------------------------

}
