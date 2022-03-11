package it.kdm.docer.commons;


import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.util.Random;

public class TicketCipher {

    private final byte[][] keys = new byte[][]{
            {29, 42, 21, 42, 100, 29, -128, 88, 12, 107, -51, -17, -99, 55, -123, 88},
            {52, 17, 119, 28, -37, -82, -9, 79, -110, -96, -51, 45, -46, 44, 2, 11},
            {-43, -126, -72, -70, -29, 84, -78, 107, -45, -110, -16, -15, 9, -119, -27, 36},
            {126, -110, 38, -76, -103, 10, 94, -1, 39, -126, -52, 81, 51, -55, -19, 65},
            {-51, 79, 81, 48, 22, 74, 120, -99, 91, -49, -107, 8, -71, 72, 41, -120}
    };

    //private SecretKeySpec skeySpec;
    //private Cipher cipher;

    private DateTime expirationDate;

    public TicketCipher() throws SecurityException {
        /*try {
            //this.skeySpec = new SecretKeySpec(this.key, "AES");
            this.cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new SecurityException(e);
        }*/
    }

    public String decryptTicket(String token) throws SecurityException {
        byte[] decrypted = Base64.decodeBase64(token.getBytes());
        return this.decryptTicket(decrypted, 0, null);
    }

    private String decryptTicket(byte[] tokenData, int i, Throwable t) throws SecurityException {

        if (i >= this.keys.length) {
            if (t == null) {
                throw new SecurityException("Token non valido");
            } else {
                throw new SecurityException("Token non valido", t);
            }
        }

        byte[] key = this.keys[i];

        String token;
        try {
            token = new String(this.runCipher(Cipher.DECRYPT_MODE, tokenData, key));
        } catch (Exception e) {
            return decryptTicket(tokenData, i + 1, e);
        }

        if (!token.contains("-|-")) {
            return decryptTicket(tokenData, i + 1, null);
        } else {
            try {

                String[] splitStr = token.split("-\\|-");
                token = splitStr[0];

                this.expirationDate = new DateTime(Long.parseLong(splitStr[1]));
                                
				/*if (date.isBeforeNow()) {
                    throw new SecurityException("Token scaduto");
				}
                                */
                return token;
            } catch (Exception e) {
                return decryptTicket(tokenData, i + 1, e);
            }
        }


    }

    public DateTime getExpirationDate() {
        return this.expirationDate;
    }

    public String encryptTicket(String token) throws SecurityException {
        return encryptTicket(token, "M2"); //default 2mesi (come da veccho codice)
    }

    public String encryptTicket(String token, String expiration) throws SecurityException {
        try {
            DateTime date = new DateTime();
            int expirationValue = 0;
            try {
                expirationValue = Integer.parseInt(expiration.substring(1));
            } catch (Exception e) {
                throw new SecurityException("Parametro 'expiration' non valido", e);
            }

            //formato expiration: [M1|D2|m30|s40]
            if (expiration.startsWith("M"))
                date = date.plusMonths(expirationValue);

            if (expiration.startsWith("D"))
                date = date.plusDays(expirationValue);

            if (expiration.startsWith("m"))
                date = date.plusMinutes(expirationValue);

            if (expiration.startsWith("s"))
                date = date.plusSeconds(expirationValue);

            token = String.format("%s-|-%d", token, date.getMillis());

            byte[] encrypted = this.runCipher(Cipher.ENCRYPT_MODE, token.getBytes(),
                    this.keys[new Random(System.nanoTime()).nextInt(this.keys.length)]);

            return new String(Base64.encodeBase64URLSafeString(encrypted));

        } catch (Exception e) {
            throw new SecurityException("Errore nella creazione del token", e);
        }
    }

    static ThreadLocal<Cipher> threadCipher = new ThreadLocal<>();

    byte[] runCipher(int mode, byte[] input, byte[] key) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = threadCipher.get();

        if (cipher==null){
            try {
                cipher = Cipher.getInstance("AES");
            } catch (Exception e) {
                throw new SecurityException(e);
            }
            threadCipher.set(cipher);
        }

        cipher.init(mode, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(input);
    }
}
