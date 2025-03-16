package me.iamkhs.passportstatuscheck.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSSenderService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioNumber;

    @Value("${twilio.recipient.number}")
    private String recipientNumber;

    private static final Logger log = LoggerFactory.getLogger(SMSSenderService.class);

    public void sendSms(String newStatus){
        String messageBody = "Congratulations\uD83C\uDF89\uD83D\uDE07, My Passport Status is Updated, " +
                "Updated Status is: " + newStatus;
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                            new PhoneNumber(recipientNumber),
                            new PhoneNumber(twilioNumber),
                            messageBody)
                    .create();

            System.out.println("Message SID: " + message.getSid());
            log.info("sms send successfully");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
