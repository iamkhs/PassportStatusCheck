package me.iamkhs.passportstatuscheck;

import me.iamkhs.passportstatuscheck.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static me.iamkhs.passportstatuscheck.service.MainService.runScript;

@SpringBootApplication
@EnableScheduling
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);
    private static String LAST_STATUS = "";
    private final EmailSenderService emailSenderService;

    public MainApplication(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Scheduled(fixedRate = 120000) // 120000 milliseconds = 2 minutes
    public void checkStatus(){
        System.out.println("inside method");
        String latestStatus = runScript();
        log.info("latest status {}", latestStatus);
        if (!LAST_STATUS.equals(latestStatus)){
            this.emailSenderService.sendMail(latestStatus);
        }
        LAST_STATUS = latestStatus;
    }

}