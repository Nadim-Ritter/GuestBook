package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import objects.Account;
import objects.SaltAndPepper;
import passwordencryption.PwEncryptionTool;

@ManagedBean(name = "Login")
@SessionScoped

public class Login {

    @ManagedProperty(value = "#{SessionBean}")
    private SessionBean sessionBean;

    private String email;
    private String password;
    private boolean loggedin;

    public Login() {

    }

    public Login(String name, String password, boolean isLoggedin) {
        this.email = null;
        this.password = null;
        this.loggedin = false;
    }

    public void reset() {
        this.email = null;
        this.password = null;
        this.loggedin = false;
    }

    public String login() throws Exception {

        Account currentAccount = checkCredentials();
        if (currentAccount.getFirstname() != null) {
            loggedin = true;

            sessionBean.setAccount(currentAccount);
            sessionBean.setLoggedIn(true);

            System.out.println("firstname: " + currentAccount.getFirstname());

            return "guestbook.xhtml?faces-redirect=true";

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Das Login ist fehlgeschlagen"));
            
            Logger logger = Logger.getLogger(Login.class.getName());
            logger.warning("Fehlgeschlagener Login Versuch: " + email);
            
            return null;
        }

    }

    public Account checkCredentials() throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String connectionURL = "jdbc:mysql://localhost/guestbook";
            Connection connection = DriverManager.getConnection(connectionURL, "root", "");

            Statement stmt = connection.createStatement();
            
            System.out.println("SELECT * FROM `account` WHERE `email` = '" + email + "' AND `password` = '" + PwEncryptionTool.encrypt((SaltAndPepper.pepper +  password) + SaltAndPepper.salt) + "' AND `activated` = 1");
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM `account` WHERE `email` = '" + email + "' AND `password` = '" + PwEncryptionTool.encrypt((SaltAndPepper.pepper +  password) + SaltAndPepper.salt) + "' AND `activated` = 1");
            Account account = new Account();

            while (resultSet.next()) {
                account.setFirstname(resultSet.getString("firstname"));
                account.setLastname(resultSet.getString("lastname"));
                account.setEmail(resultSet.getString("email"));
                account.setAccountId(resultSet.getInt("id"));
                account.setRole(resultSet.getString("role"));
            }

            resultSet.close();
            stmt.close();
            connection.close();

            return account;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String logout() {
        sessionBean.setLoggedIn(false);
        sessionBean.setAccount(null);
        
        return "logout.xhtml?faces-redirect=true";
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

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

}
