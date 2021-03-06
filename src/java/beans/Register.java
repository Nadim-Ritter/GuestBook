package beans;

import authorisation.VerifyRecaptcha;
import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import java.util.logging.Logger;

import objects.Account;
import objects.SaltAndPepper;
import passwordencryption.PwEncryptionTool;

@ManagedBean(name = "Register")
@RequestScoped
public class Register {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passwordCheck;
    private String uuid;
    private String role;

    public String submit_form() {
        if (password.length() >= 7) {
            if (password.equals(passwordCheck)) {
                try {
                    String gRecaptchaResponse = FacesContext.getCurrentInstance().
                            getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
                    boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
                    if (verify) {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("sucees Captcha"));
                        createAccount();
                        return "register";
                    } else {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Select Captcha"));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                return null;

            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Die Passwörter sind nicht gleich"));
                return null;
            }

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Das Passwort erreicht nicht die gewünschten Anforderungen"));
            return null;

        }

    }

    public void createAccount() throws SQLException, MessagingException, Exception {

        UUID uuid = UUID.randomUUID();

        Account account = new Account(firstname, lastname, email, password, false, uuid.toString(), role);

        if (insertIntoDB(account)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Eine E-Mail wurde an " + email + " gesendet, bitte klicken sie auf den Aktivierungslink"));

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Die Registrierung ist fehlgeschlagen"));
        }

    }

    public boolean insertIntoDB(Account account) throws SQLException, MessagingException, Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String connectionURL = "jdbc:mysql://localhost/guestbook";
            Connection connection = DriverManager.getConnection(connectionURL, "root", "");

            String passwordWithSalt = PwEncryptionTool.encrypt((SaltAndPepper.pepper + account.getPassword()) + SaltAndPepper.salt);

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO `account` (`firstname`, `lastname`, `email`, `password`, `activated`, `uuid`, `role`) VALUES ('" + account.getFirstname() + "', '" + account.getLastname() + "', '" + account.getEmail() + "', '" + passwordWithSalt + "', " + 0 + ", '" + account.getUuid() + "', '" + account.getRole() + "')");
            System.out.println("INSERT INTO `account` (`firstname`, `lastname`, `email`, `password`, `activated`, `uuid`, `role`) VALUES ('" + account.getFirstname() + "', '" + account.getLastname() + "', '" + account.getEmail() + "', '" + passwordWithSalt + "', " + 0 + ", '" + account.getUuid() + "', '" + account.getRole() + "')");

            MailHandler mailHandler = new MailHandler();
            mailHandler.sendConfirmationMail(account);

            //ResultSet entries = stmt.executeQuery("SELECT * FROM time");
            //entries.close();
            stmt.close();
            connection.close();
            
            Logger logger = Logger.getLogger(Register.class.getName());
            logger.info("New Account registred " + account.getEmail());
            
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void activateAccount() {
        if (uuid != null) {
            try {
                System.out.println("uuid: " + uuid);
                Class.forName("com.mysql.cj.jdbc.Driver");

                String connectionURL = "jdbc:mysql://localhost/guestbook";
                Connection connection = DriverManager.getConnection(connectionURL, "root", "");

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE `account` SET `activated` = '1' WHERE `account`.`uuid` = '" + uuid + "'");
                System.out.println("UPDATE `account` SET `activated` = '1' WHERE `account`.`uuid` = '" + uuid + "'");

                stmt.close();
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("uuid: " + uuid);
        }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
