package keysuite.docer.utils;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class PasswordUtils {

    private static final String SECRET = "SECRET";


    public static String hashPassword(String password){
        password = password+SECRET;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            password = DatatypeConverter.printHexBinary(digest).toUpperCase();
        }catch (Exception e){

        }

        return password;
    }

}
