package beans;

import authorisation.VerifyRecaptcha;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Register {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passwordConfirm;

    public Register(String prename, String lastname, String email, String password, String passwordConfirm) {
        this.firstname = prename;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String submit_form() {
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
            boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
            if (verify) {
                return "Success";
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Select Captcha"));
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    
    

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

}
