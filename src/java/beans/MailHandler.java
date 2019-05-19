package beans;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import objects.Account;

public class MailHandler {
    
    public void sendConfirmationMail(Account account) throws MessagingException {
        final String username = "testemailadressritter@gmail.com";
        final String password = "TestBaum1234";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message messageObj = new MimeMessage(session);
            messageObj.setFrom(new InternetAddress(username));
            messageObj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account.getEmail()));
            messageObj.setSubject("Guestbook: Bitte bestätigen sie ihre E-Mail Adresse");
            messageObj.setText("http://localhost:8080/GuestBook/register.xhtml?uuid=" +account.getUuid());

            Transport.send(messageObj);

            //System.out.println("Sent email-activitychange-notification to: " + recipientAccount.geteMail());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
