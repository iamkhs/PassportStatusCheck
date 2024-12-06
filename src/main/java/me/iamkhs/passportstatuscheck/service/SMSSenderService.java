package me.iamkhs.passportstatuscheck.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SMSSenderService {

    private static final Logger log = LoggerFactory.getLogger(SMSSenderService.class);

    public void sendSms(String newStatus){
        final String ACCOUNT_SID = "AC022c5f0fda518517aec0b56798b9ed85";
        final String AUTH_TOKEN = "0cd4b88e94f93839d18ba86ffe1306d8";
        final String twilioNumber = "+12025590250";
        final String recipientNumber = "+8801704546944";
        String messageBody = "Congratulations\uD83C\uDF89\uD83D\uDE07, My Passport Status is Updated, " +
                "Updated Status is: " + newStatus;
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
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
