package me.iamkhs.passportstatuscheck.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MainService {

    private static final Logger log = LoggerFactory.getLogger(MainService.class);
    private static final RestTemplate restTemplate = new RestTemplate();
    private final static String API_URL = "https://www.epassport.gov.bd/api/v1/applications/check";


    // This method is returning the current status of my application
    public static String runScript() {

        try {
            String captchaResponse = HCaptchaAutomation.getCaptchaResponse();
            log.info("captcha {}", captchaResponse);

            // build headers
            HttpEntity<MultiValueMap<String, String>> requestEntity = getMultiValueMapHttpEntity(captchaResponse);

            // Make POST request and get the response as a String
            String response = restTemplate.postForObject(API_URL, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode contentNode = rootNode.get("content");

            return contentNode.get("status").asText();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    // building the post request
    private static HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String captchaResponse) {
        HttpHeaders headers = getHttpHeaders();

        // Prepare body as a Map (for form-urlencoded)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("id", "4101-000440115");
        body.add("dob", "2002-12-31");
        body.add("recaptcha", captchaResponse);

        return new HttpEntity<>(body, headers);
    }

    // building the http headers for post request
    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.set("Accept-Language", "en-GB,en;q=0.9");
        headers.set("Origin", "https://www.epassport.gov.bd");
        headers.set("Referer", "https://www.epassport.gov.bd/authorization/application-status");
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("Sec-GPC", "1");
        headers.set("Sec-CH-UA", "\"Brave\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
        headers.set("Sec-CH-UA-Mobile", "?0");
        headers.set("Sec-CH-UA-Platform", "\"macOS\"");
        headers.set("Connection", "keep-alive");
        return headers;
    }

}
