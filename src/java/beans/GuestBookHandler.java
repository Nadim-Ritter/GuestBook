/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import objects.Account;
import objects.GuestBookEntry;

@ManagedBean(name = "guestbookHandler")
@RequestScoped

public class GuestBookHandler {

    @ManagedProperty(value = "#{SessionBean}")
    private SessionBean sessionBean;

    private String text;
    private ArrayList<GuestBookEntry> guestBookEntries = new ArrayList();
    private boolean statusGood;
    private boolean statusOkey;
    private boolean statusBad;

    public GuestBookHandler() {

    }
    /*
    if (statusGood) status = "good";
            else if (statusOkey) status = "okey";
            else if (statusBad) status = "bad";
    */

    public void createGuestBookEntry() throws SQLException {

        if (sessionBean.isLoggedIn()) {

            String status = "nothing";

            if (statusGood) status = "good";
            else if (statusOkey) status = "okey";
            else if (statusBad) status = "bad";
            

            if (!status.equals("nothing")) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    String connectionURL = "jdbc:mysql://localhost/guestbook";
                    Connection connection = DriverManager.getConnection(connectionURL, "root", "");

                    Statement stmt = connection.createStatement();

                    
                    System.out.println(status);
                    
                    
                    System.out.println("INSERT INTO `guestbook_entry`(`accountID`, `text`, `status`) VALUES (" + sessionBean.getAccount().getAccountId() + ", '" + text + "', '" + status + "')");
                    stmt.executeUpdate("INSERT INTO `guestbook_entry`(`accountID`, `text`, `status`) VALUES (" + sessionBean.getAccount().getAccountId() + ", '" + text + "', '" + status + "')");

                    stmt.close();
                    connection.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Wählen sie einen Status aus"));

            }
        }
    }

    public void loadGuestBookList() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String connectionURL = "jdbc:mysql://localhost/guestbook";
            Connection connection = DriverManager.getConnection(connectionURL, "root", "");

            Statement stmt = connection.createStatement();

            System.out.println("");
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM `guestbook_entry`");

            while (resultSet.next()) {
                Statement stmt2 = connection.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery("SELECT * FROM `account` WHERE `id` = " + resultSet.getInt("accountID"));
                while (resultSet2.next()) {
                    GuestBookEntry entry = new GuestBookEntry(resultSet2.getString("firstname"), resultSet2.getString("lastname"), resultSet.getString("text"));
                    guestBookEntries.add(entry);
                }
                resultSet2.close();
                stmt2.close();
            }

            resultSet.close();
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public ArrayList<GuestBookEntry> getGuestBookEntries() {
        return guestBookEntries;
    }

    public void setGuestBookEntries(ArrayList<GuestBookEntry> guestBookEntries) {
        this.guestBookEntries = guestBookEntries;
    }

    public boolean isStatusGood() {
        return statusGood;
    }

    public void setStatusGood(boolean statusGood) {
        this.statusGood = statusGood;
    }

    public boolean isStatusOkey() {
        return statusOkey;
    }

    public void setStatusOkey(boolean statusOkey) {
        this.statusOkey = statusOkey;
    }

    public boolean isStatusBad() {
        return statusBad;
    }

    public void setStatusBad(boolean statusBad) {
        this.statusBad = statusBad;
    }

}
