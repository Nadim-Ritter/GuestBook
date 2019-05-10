package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "Login")
@SessionScoped

public class Login {
    
    private String name;
    private String password;
    private boolean loggedin;
    
    public Login(){
        
    }

    public Login(String name, String password, boolean isLoggedin) {
        this.name = null;
        this.password = null;
        this.loggedin = false;
    }
    
    public void reset(){
        this.name = null;
        this.password = null;
        this.loggedin = false;
    }
    
    public String login(){
        loggedin = true;
        return "guestbook.xhtml?faces-redirect=true";
    }
    
    public String logout(){
        reset();
        return "logout.xhtml?faces-redirect=true";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    
    
    
    

    

}
