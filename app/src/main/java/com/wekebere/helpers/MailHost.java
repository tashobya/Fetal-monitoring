package com.wekebere.helpers;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailHost {
public static void send(final String from_user, final String password_email, String to_email, String sub_email, String msg_email){
//Get properties object    
Properties props = new Properties();    
props.put("mail.smtp.host", "smtp.gmail.com");    
props.put("mail.smtp.socketFactory.port", "465");    
props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");    
props.put("mail.smtp.auth", "true");    
props.put("mail.smtp.port", "465");    
//...........get Session   
Session session = Session.getDefaultInstance(props,    
new javax.mail.Authenticator() {    
protected PasswordAuthentication getPasswordAuthentication() {    
return new PasswordAuthentication(from_user,password_email);
}    
});    
//..........compose message    
try {    
MimeMessage message = new MimeMessage(session);    
message.addRecipient(Message.RecipientType.TO,new InternetAddress(to_email));
message.setSubject(sub_email);
message.setText(msg_email);
//send message  
Transport.send(message);    
} catch (MessagingException e) {
//.................AVODING DEBUGGS................................
}
}
}
