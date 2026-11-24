package fr.td;

import static fr.td.Verificateur.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import fr.td.cours.Cours;
import fr.td.cours.CoursId;
import fr.td.formules.Abonnement;
import fr.td.formules.Entrees;
import fr.td.formules.Montant;
import fr.td.formules.MotifDeRefus;
import fr.td.formules.ResultatDePaiement.PaiementAccepte;
import fr.td.formules.ResultatDePaiement.PaiementRefuse;
import fr.td.formules.paysur.ReponsePaySur;
import fr.td.formules.paysur.TraducteurPaySur;
import fr.td.inscription.GrimpeurId;
import fr.td.reservation.Creneau;
import fr.td.reservation.CreneauReserve;
import fr.td.reservation.Grimpeur;
import fr.td.reservation.ReservationAnnulee;

/**
 * VOS vérifications. Lancez ce programme (clic droit > Run 'Verifications.main()').
 *
 * Une vérification par règle, au minimum. Chacune montre que la règle est
 * respectée... et qu'on ne peut pas la contourner.
 */
public class Verifications {

    private static final LocalDateTime LE_1ER_MARS_10H = LocalDateTime.of(2026, 3, 1, 10, 0);
    private static final LocalDate LE_1ER_MARS = LE_1ER_MARS_10H.toLocalDate();

    public static void main(String[] args) {
        titre("Value objects");
        verifier("G2", "un créneau refuse une heure qui n'est pas fixe",
                () -> refuse(() -> new Creneau(LocalDateTime.of(2026, 3, 1, 11, 0))));
        verifier("VO", "Entrees refuse un nombre negatif",
                () -> refuse(() -> new Entrees(-1)));
        verifier("VO", "deux identifiants de grimpeur vides sont refusés",
                () -> refuse(() -> new GrimpeurId("")));
        verifier("VO", "un montant négatif est refusé",
                () -> refuse(() -> Montant.euros("-1")));

        titre("Réservation (core) : Grimpeur");
        verifier("G4", "un grimpeur ne peut pas avoir plus de 2 reservations",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 6, 20, 0)), LE_1ER_MARS_10H));
                });
        verifier("G3", "un porteur de carte ne réserve pas plus de 48 h à l'avance",
                () -> {
                    Grimpeur grimpeur = grimpeur("G4", Grimpeur.Formule.PORTEUR_DE_CARTE);
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 4, 14, 0)), LE_1ER_MARS_10H));
                });
        verifier("G17", "un abonne suspendu ne peut plus reserver",
                () -> {
                    Grimpeur grimpeur = grimpeur("G2", Grimpeur.Formule.ABONNE);
                    grimpeur.suspendreAbonnement();
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 2, 14, 0)), LE_1ER_MARS_10H));
                });
        verifier("G17", "la regularisation rend de nouveau possible la reservation",
                () -> {
                    Grimpeur grimpeur = grimpeur("G3", Grimpeur.Formule.ABONNE);
                    grimpeur.suspendreAbonnement();
                    grimpeur.regulariserAbonnement();
                    grimpeur.reserver(new Creneau(LocalDateTime.of(2026, 3, 2, 14, 0)), LE_1ER_MARS_10H);
                    attendu(1, grimpeur.reservations().size());
                });

        titre("Réservation (core) : événements");
        verifier("EV1", "une réservation acceptée produit CreneauReserve",
                () -> {
                    Grimpeur grimpeur = grimpeur("G5", Grimpeur.Formule.ABONNE);
                    Creneau samedi14h = new Creneau(LocalDateTime.of(2026, 3, 7, 14, 0));
                    attendu(new CreneauReserve(grimpeur.id(), samedi14h),
                            grimpeur.reserver(samedi14h, LE_1ER_MARS_10H));
                    attendu(1, grimpeur.reservations().size());
                });
        verifier("EV1", "une réservation refusée (G4) ne change rien",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    refuse(() -> grimpeur.reserver(
                            new Creneau(LocalDateTime.of(2026, 3, 6, 20, 0)), LE_1ER_MARS_10H));
                    attendu(2, grimpeur.reservations().size());
                });
        verifier("EV2", "une annulation plus de 24 h avant produit ReservationAnnulee non tardive",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    Creneau lundi10h = new Creneau(LocalDateTime.of(2026, 3, 2, 10, 0));
                    attendu(new ReservationAnnulee(grimpeur.id(), lundi10h, false),
                            grimpeur.annuler(lundi10h, LocalDateTime.of(2026, 3, 1, 9, 0)));
                    attendu(1, grimpeur.reservations().size());
                });
        verifier("G8", "une annulation moins de 24 h avant produit ReservationAnnulee tardive",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    Creneau lundi10h = new Creneau(LocalDateTime.of(2026, 3, 2, 10, 0));
                    attendu(new ReservationAnnulee(grimpeur.id(), lundi10h, true),
                            grimpeur.annuler(lundi10h, LocalDateTime.of(2026, 3, 1, 20, 0)));
                });
        verifier("EV2", "annuler un créneau non réservé est refusé et ne change rien",
                () -> {
                    Grimpeur grimpeur = abonneAvecDeuxReservations();
                    refuse(() -> grimpeur.annuler(
                            new Creneau(LocalDateTime.of(2026, 3, 5, 18, 0)), LE_1ER_MARS_10H));
                    attendu(2, grimpeur.reservations().size());
                });

        titre("Cours collectifs : Cours");
        verifier("G10", "un cours complet refuse un neuvieme grimpeur",
                () -> {
                    Cours cours = coursAvecHuitGrimpeurs();
                    refuse(() -> cours.inscrire(new GrimpeurId("G9")));
                });
        verifier("G10", "une place liberee peut etre redonnée",
                () -> {
                    Cours cours = coursAvecHuitGrimpeurs();
                    GrimpeurId premier = cours.participants().iterator().next();
                    cours.desinscrire(premier);
                    cours.inscrire(new GrimpeurId("G10"));
                    attendu(8, cours.participants().size());
                });

        titre("Inscription et badges : un mot, deux modèles");
        verifier("G15", "un mineur sans accompagnateur n'est pas inscrit",
                () -> refuse(() -> fr.td.inscription.Grimpeur.inscrire(
                        new GrimpeurId("JADE"), "Jade", LocalDate.of(2014, 1, 1), LE_1ER_MARS)));
        verifier("G15", "un mineur est inscrit avec l'adulte qui l'accompagne",
                () -> {
                    fr.td.inscription.Grimpeur nadia = fr.td.inscription.Grimpeur.inscrire(
                            new GrimpeurId("NADIA"), "Nadia", LocalDate.of(1990, 5, 12), LE_1ER_MARS);
                    fr.td.inscription.Grimpeur jade = fr.td.inscription.Grimpeur.inscrireMineur(
                            new GrimpeurId("JADE"), "Jade", LocalDate.of(2014, 1, 1), nadia, LE_1ER_MARS);
                    attendu(nadia.id(), jade.accompagnateur().orElseThrow());
                });
        verifier("BC", "Grimpeur d'Inscription et Grimpeur de Réservation partagent un identifiant, pas un objet",
                () -> {
                    GrimpeurId id = new GrimpeurId("NADIA");
                    fr.td.inscription.Grimpeur alAccueil = fr.td.inscription.Grimpeur.inscrire(
                            id, "Nadia", LocalDate.of(1990, 5, 12), LE_1ER_MARS);
                    Grimpeur enReservation = new Grimpeur(id, Grimpeur.Formule.PORTEUR_DE_CARTE);
                    attendu(alAccueil.id(), enReservation.id());
                    differents(alAccueil, enReservation);
                });

        titre("ACL PaySûr → Formules et abonnements");
        TraducteurPaySur traducteur = new TraducteurPaySur();
        verifier("ACL", "PAID et 5900 centimes deviennent un PaiementAccepte de 59,00 €",
                () -> attendu(new PaiementAccepte("TX-1", Montant.euros("59.00")),
                        traducteur.traduire(new ReponsePaySur("PAID", 5900, "TX-1"))));
        verifier("ACL", "FAILED_NSF devient un PaiementRefuse pour provision insuffisante",
                () -> attendu(new PaiementRefuse("TX-2", MotifDeRefus.PROVISION_INSUFFISANTE),
                        traducteur.traduire(new ReponsePaySur("FAILED_NSF", 5900, "TX-2"))));
        verifier("ACL", "un code PaySûr inconnu ne passe pas en silence",
                () -> refuse(() -> traducteur.traduire(new ReponsePaySur("CHARGEBACK", 5900, "TX-3"))));
        verifier("ACL", "un code absent ne passe pas en silence",
                () -> refuse(() -> traducteur.traduire(new ReponsePaySur(null, 0, "TX-4"))));
        verifier("G17", "le prélèvement de Hugo refusé par PaySûr suspend son abonnement",
                () -> {
                    Abonnement hugo = new Abonnement(new GrimpeurId("HUGO"));
                    hugo.prendreEnCompte(traducteur.traduire(new ReponsePaySur("FAILED_NSF", 5900, "TX-5")));
                    attendu(true, hugo.estSuspendu());
                });
        verifier("G17", "Hugo régularise : le paiement accepté lève la suspension",
                () -> {
                    Abonnement hugo = new Abonnement(new GrimpeurId("HUGO"));
                    hugo.prendreEnCompte(traducteur.traduire(new ReponsePaySur("FAILED_NSF", 5900, "TX-5")));
                    hugo.prendreEnCompte(traducteur.traduire(new ReponsePaySur("PAID", 5900, "TX-6")));
                    attendu(false, hugo.estSuspendu());
                });

        bilan();
    }

    // ---------------------------------------------------------------------
    private static Grimpeur grimpeur(String id, Grimpeur.Formule formule) {
        return new Grimpeur(new GrimpeurId(id), formule);
    }

    private static Grimpeur abonneAvecDeuxReservations() {
        Grimpeur grimpeur = grimpeur("G1", Grimpeur.Formule.ABONNE);
        grimpeur.reserver(new Creneau(LocalDateTime.of(2026, 3, 2, 10, 0)), LE_1ER_MARS_10H);
        grimpeur.reserver(new Creneau(LocalDateTime.of(2026, 3, 3, 12, 0)), LE_1ER_MARS_10H);
        return grimpeur;
    }

    private static Cours coursAvecHuitGrimpeurs() {
        Cours cours = new Cours(new CoursId("C1"));
        for (int i = 1; i <= 8; i++)
            cours.inscrire(new GrimpeurId("G" + i));
        return cours;
    }
    // ---------------------------------------------------------------------

}
