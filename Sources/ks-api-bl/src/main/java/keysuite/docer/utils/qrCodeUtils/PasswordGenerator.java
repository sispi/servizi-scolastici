package keysuite.docer.utils.qrCodeUtils;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

public class PasswordGenerator {
    static Random rnd = new Random();

    public static String getPWD() {


        //decido dimensione password
        int dim = 8;//rnd.nextInt(5);
        //dim = dim+6;
        //decido la quantita' di cifre numeriche
        int num = rnd.nextInt(dim-4)+1;
        num= num+2;
        //decido la quantita' di cifre alfa
        int alfa = dim-num;
        int appoggio=0;
        String pass="";
        int alterna=0;
        int contnum=1;
        int contalfa=1;
        for (int i=1;i<=dim;i++) {
            alterna = rnd.nextInt(2);
            if (contalfa>alfa){alterna=0;}
            else{if (contnum>num) {alterna=1;}
            }
            if (alterna==1) {contalfa++;
                appoggio = rnd.nextInt(25);
                switch (appoggio) {
                    case 0 : pass = pass+"A";break;
                    case 1 : pass = pass+"B";break;
                    case 2 : pass = pass+"C";break;
                    case 3 : pass = pass+"D";break;
                    case 4 : pass = pass+"E";break;
                    case 5 : pass = pass+"F";break;
                    case 6 : pass = pass+"G";break;
                    case 7 : pass = pass+"H";break;
                    case 8 : pass = pass+"I";break;
                    case 9 : pass = pass+"J";break;
                    case 10 : pass = pass+"K";break;
                    case 11 : pass = pass+"L";break;
                    case 12 : pass = pass+"M";break;
                    case 13 : pass = pass+"N";break;
                    case 14 : pass = pass+"O";break;
                    case 15 : pass = pass+"P";break;
                    case 16 : pass = pass+"Q";break;
                    case 17 : pass = pass+"R";break;
                    case 18 : pass = pass+"S";break;
                    case 19 : pass = pass+"T";break;
                    case 20 : pass = pass+"U";break;
                    case 21 : pass = pass+"W";break;
                    case 22 : pass = pass+"X";break;
                    case 23 : pass = pass+"Y";break;
                    case 24 : pass = pass+"Z";break;
                }
            }
            if (alterna==0) {contnum++;
                appoggio = rnd.nextInt(10);
                switch (appoggio) {
                    case 0 : pass = pass+"0";break;
                    case 1 : pass = pass+"1";break;
                    case 2 : pass = pass+"2";break;
                    case 3 : pass = pass+"3";break;
                    case 4 : pass = pass+"4";break;
                    case 5 : pass = pass+"5";break;
                    case 6 : pass = pass+"6";break;
                    case 7 : pass = pass+"7";break;
                    case 8 : pass = pass+"8";break;
                    case 9 : pass = pass+"9";break;
                }
            }
        }

        return pass;
    }

    @SuppressWarnings("restriction")
    public static String crypt(String s, String tipo){
        String str = null;
        try{
            MessageDigest md = MessageDigest.getInstance(tipo);
            md.reset();
            md.update(s.getBytes("UTF-8"));
            str = new String(Base64.getEncoder().encode(md.digest()), "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public static String sha256(String base) {
        String result = crypt(base, "SHA-256");
        return result;
    }
    public static boolean passwordvalidation(String password) {
//        String passwd = "aaZZa44@";
        String numero = "(?=.*[0-9])";
        String lowerChar = "(?=.*[a-z])";
        String upperChar = "(?=.*[A-Z])";
        String specialChar = "(?=.*[@#$%^&+=])";
        String excludeWhiteSpace = "(?=\\S+$)";
        String minMaxLenght = ".{8,15}";
        String pattern = numero+lowerChar+upperChar+excludeWhiteSpace+minMaxLenght;//"(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,10}";
        return password.matches(pattern);
    }
}
