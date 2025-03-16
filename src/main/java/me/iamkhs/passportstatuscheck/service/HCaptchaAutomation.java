package me.iamkhs.passportstatuscheck.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

public class HCaptchaAutomation {

    private static final Logger log = LoggerFactory.getLogger(HCaptchaAutomation.class);

    public static String getCaptchaResponse() {
//        System.setProperty("webdriver.chrome.driver", "/Users/iamkhs/Desktop/chromedriver-mac-arm64/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // Configure ChromeDriver
        ChromeDriver driver = getChromeDriver();

        try {
            // Navigate to the target webpage
            driver.get("https://www.epassport.gov.bd/authorization/application-status");

            // Wait for the iframe containing the hCaptcha to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased wait time
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[src*='hcaptcha']")));

            // Switch to the iframe to interact with its content
            driver.switchTo().frame(iframe);

            // Locate the checkbox element inside the iframe
            WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkbox")));

            // Simulate a click on the checkbox using JavascriptExecutor
            driver.executeScript("arguments[0].click();", checkbox);

            // Now switch back to the main document
            driver.switchTo().defaultContent();

            // Wait for the iframe element again (you can customize the wait time)
            WebElement iframeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[src*='hcaptcha']")));

            // Wait until the data-hcaptcha-response attribute is available
            WebDriverWait waitForResponse = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitForResponse.until(ExpectedConditions.attributeToBeNotEmpty(iframeElement, "data-hcaptcha-response"));

            // Print the iframe HTML to check if it's located correctly
            return Objects.requireNonNull(iframeElement.getAttribute("data-hcaptcha-response"));

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // Quit the driver
            driver.quit();
        }
        return null;
    }

    private static ChromeDriver getChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless",
                "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--remote-debugging-port=9222",
                "--window-size=1920x1080");
        return new ChromeDriver(options);
    }

}