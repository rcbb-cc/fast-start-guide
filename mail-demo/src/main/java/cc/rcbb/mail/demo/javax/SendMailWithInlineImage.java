package cc.rcbb.mail.demo.javax;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class SendMailWithInlineImage {

    public static void main(String[] args) throws Exception {
        final String smtp = "smtp.office365.com";
        final String username = "jxsmtp101@outlook.com";
        final String password = "java-12345678";
        final String from = "jxsmtp101@outlook.com";
        final String to = "javacourse001@sina.com";
        SendMail sender = new SendMail(smtp, username, password);
        Session session = sender.createSSLSession();
        try (InputStream input = SendMailWithInlineImage.class.getResourceAsStream("/javamail.jpg")) {
            Message message = createMessageWithInlineImage(session, from, to, "Hello Java HTML邮件内嵌图片",
                    "<h1>Hello</h1><p><img src=\"cid:img01\"></p><p>这是一封内嵌图片的<u>javamail</u>邮件！</p>", "javamail.jpg", input);
            Transport.send(message);
        }
    }

    static Message createMessageWithInlineImage(Session session, String from, String to, String subject, String body, String fileName, InputStream input)
            throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "UTF-8");
        Multipart multipart = new MimeMultipart();
        // 添加text:
        BodyPart textpart = new MimeBodyPart();
        textpart.setContent(body, "text/html;charset=utf-8");
        multipart.addBodyPart(textpart);
        // 添加image:
        BodyPart imagepart = new MimeBodyPart();
        imagepart.setFileName(fileName);
        imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "image/jpeg")));
        // 与HTML的<img src="cid:img01">关联:
        imagepart.setHeader("Content-ID", "<img01>");
        multipart.addBodyPart(imagepart);
        message.setContent(multipart);
        return message;
    }

}
