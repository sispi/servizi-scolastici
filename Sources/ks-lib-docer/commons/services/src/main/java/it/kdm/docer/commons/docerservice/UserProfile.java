package it.kdm.docer.commons.docerservice;

/**
 * Created by Łukasz Kwasek on 15/01/15.
 */
public class UserProfile {

    public UserProfile(String userID) {
        this.userID = userID;
    }

    public UserProfile() {
    }

    public UserProfile(String userID, String codiceEnte, String password) {
        this(userID, codiceEnte, password, null);
    }

    public UserProfile(String userID, String codiceEnte, String password, String email) {
        this.userID = userID;
        this.codiceEnte = codiceEnte;
        this.password = password;
        this.email = email;
    }

    private String userID;
    private String codiceEnte;
    private String password;
    private String email;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public void setCodiceEnte(String codiceEnte) {
        this.codiceEnte = codiceEnte;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
