package fr.td.inscription;

public class InscriptionRefusee extends RuntimeException {
    public InscriptionRefusee(String message) {
        super(message);
    }
}
