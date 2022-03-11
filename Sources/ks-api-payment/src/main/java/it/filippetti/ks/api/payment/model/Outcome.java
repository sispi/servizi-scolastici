/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

/**
 *
 * @author dino
 */
public enum Outcome {
    READY { // Appena creato
        @Override
        public boolean isReady() {
            return true;
        }
    },
    WAITING { // Pagina di cassa richiesta, iuv e url generate.
        @Override
        public boolean isWaiting() {
            return true;
        }
    },
    COMPLETED { // Pagamento completato ma ricevuta non arrivata
        @Override
        public boolean isCompleted() {
            return true;
        }
    },
    RECEIPT { // Pagamento completato e ricevuta arrivata
        @Override
        public boolean isReceipt() {
            return true;
        }
    },
    CANCELED { // Pagamento annullato dall'utente
        @Override
        public boolean isCanceled() {
            return true;
        }
    },
    FAILED { // Pagamento fallito
        @Override
        public boolean isFailed() {
            return true;
        }
    };

    public boolean isReady() { return false; }

    public boolean isWaiting() { return false; }

    public boolean isCompleted() { return false; }

    public boolean isReceipt() { return false; }

    public boolean isCanceled() { return false; }

    public boolean isFailed() { return false; }

}
