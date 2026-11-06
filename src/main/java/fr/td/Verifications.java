package fr.td;

import static fr.td.Verificateur.*;

import java.time.LocalDate;

// Vos classes du package fr.td.domaine s'importent ici (IntelliJ le fait pour vous : Alt+Entrée).

/**
 * VOS vérifications. Lancez ce programme (clic droit > Run 'Verifications.main()').
 *
 * Une vérification par règle, au minimum. Chacune montre que la règle est
 * respectée... et qu'on ne peut pas la contourner.
 */
public class Verifications {

    public static void main(String[] args) {

        // ------------------------------------------------------------------
        // Pour voir comment fonctionnent les outils. Supprimez ce bloc quand
        // vous avez compris : il ne vérifie rien de votre domaine.
        // ------------------------------------------------------------------
        titre("Exemple : comment écrire une vérification");
        verifier("EX1", "attendu : la valeur obtenue est celle attendue",
                () -> attendu(3, "abc".length()));
        verifier("EX2", "refuse : une date impossible est refusée",
                () -> refuse(() -> LocalDate.of(2026, 2, 30)));
        verifier("EX3", "differents : deux dates différentes ne sont pas égales",
                () -> differents(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 2)));

        // ------------------------------------------------------------------
        // Vos vérifications. Le code, c'est celui de la règle du sujet
        // (« G4 », « V3 », « S4 », « A8 ») ou de votre hypothèse (« H2 »).
        //
        // Exemple sur MédiaTech (ne compile pas ici, c'est pour la forme) :
        //
        //   titre("Adherent (entité)");
        //   verifier("R1", "un adhérent ne peut pas avoir 4 emprunts en cours", () -> {
        //       Adherent adherent = adherentAvecTroisEmprunts();
        //       refuse(() -> adherent.emprunter(new ExemplaireId("E4"), AUJOURDHUI));
        //   });
        // ------------------------------------------------------------------



        bilan();
    }

    // ---------------------------------------------------------------------
    // Vos méthodes utilitaires : elles fabriquent des objets prêts à l'emploi
    // pour vos vérifications (par exemple adherentAvecTroisEmprunts()).
    // ---------------------------------------------------------------------

}
