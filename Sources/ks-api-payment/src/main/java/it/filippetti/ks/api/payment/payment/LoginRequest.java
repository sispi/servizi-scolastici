package it.filippetti.ks.api.payment.payment;

import static it.filippetti.ks.api.payment.payment.PayParam.Service.*;
import java.util.Date;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public abstract class LoginRequest {

    // login
    private String username;
    private String password;
    private Date requestDate;
    private String requestId;
    private String companyId;

    /**
     * username for login<BR>
     *
     * @return username
     */
    @PayParam(service = PMPAY, param = "idClient")
    public String getUsername() {
        return username;
    }

    /**
     * set username for login
     *
     * @param username
     *
     */
    @PayParam(service = PMPAY, param = "idClient")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * password for login
     *
     * @return password for login
     */
    @PayParam(service = PMPAY, param = "pwdClient")
    public String getPassword() {
        return password;
    }

    /**
     * set password for login
     *
     * @param password
     *
     */
    @PayParam(service = PMPAY, param = "pwdClient")
    public void setPassword(String password) {
        this.password = password;
    }

    @PayParam(service = PMPAY, param = "dataRichiesta")
    public Date getRequestDate() {
        return requestDate;
    }

    @PayParam(service = PMPAY, param = "dataRichiesta")
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @PayParam(service = PMPAY, param = "idRichiesta")
    public String getRequestId() {
        return requestId;
    }

    @PayParam(service = PMPAY, param = "idRichiesta")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @PayParam(service = PMPAY, param = "codiceAzienda")
    public String getCompanyId() {
        return companyId;
    }

    @PayParam(service = PMPAY, param = "codiceAzienda")
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
