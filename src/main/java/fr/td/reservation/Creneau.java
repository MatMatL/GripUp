package fr.td.reservation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

/** G2 : un créneau dure 2 heures, à heures fixes. */
public record Creneau(LocalDateTime debut) {
    public static final Duration DUREE = Duration.ofHours(2);
    private static final Set<Integer> HEURES_FIXES = Set.of(10, 12, 14, 16, 18, 20);

    public Creneau {
        if (debut == null)
            throw new IllegalArgumentException("le debut du creneau est obligatoire");
        if (!HEURES_FIXES.contains(debut.getHour())
                || debut.getMinute() != 0
                || debut.getSecond() != 0
                || debut.getNano() != 0)
            throw new IllegalArgumentException("G2 : le creneau doit commencer a une heure fixe");
    }

    public LocalDateTime fin() {
        return debut.plus(DUREE);
    }

    public boolean estAVenir(LocalDateTime maintenant) {
        return fin().isAfter(maintenant);
    }

    public boolean estCommence(LocalDateTime maintenant) {
        return !maintenant.isBefore(debut);
    }
}
