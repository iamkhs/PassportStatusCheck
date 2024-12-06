package me.iamkhs.passportstatuscheck.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String newStatus){
        try {
            String mailBody = "Congratulations\uD83C\uDF89\uD83D\uDE07, My Passport Status is Updated; " +
                    "Updated Status is: <h2>" + newStatus+"<h2>";
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.addHeader("X-Priority", "1"); // 1 = High priority
            mimeMessage.addHeader("Importance", "high");

            messageHelper.setTo("shantohossain.p@gmail.com");
            messageHelper.setText(mailBody ,true);
            messageHelper.setSubject("Congratulations \uD83C\uDF89\uD83D\uDE07, Status Updated\uD83E\uDD17");

            mailSender.send(mimeMessage);
            log.info("Mail Send Successfully");
        }catch (Exception e){
            log.error("Email Sending Failed! {}", e.getMessage());
        }
    }
}
