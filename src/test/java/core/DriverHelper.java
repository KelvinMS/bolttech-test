package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DriverHelper {


    private static final String PROPERTYCHROME = "webdriver.chrome.driver";
    private static final String PROPERTYFIREFOX = "webdriver.gecko.driver";
    private static final String GECKODRIVER_PATH = "" + Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "drivers", "geckodriver.exe");
    private static final String CHROMEDRIVER_PATH = "" + Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "drivers", "chromedriver.exe");

    public WebDriver setupDriver(String browser) {
        browser = browser.toLowerCase();
        WebDriver driver;
        setupDriverPaths();
        DesiredCapabilities des = new DesiredCapabilities();
        des.setBrowserName(browser);
        Map<String, String> deviceMobEmulator = new HashMap<>();
        deviceMobEmulator.put("deviceName", "iPhone XR"); // Other phone to test Pixel 5


        if (browser.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("mobileEmulation",deviceMobEmulator);
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
        } else {
            // Under development
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null"); //Remove checkodriver log
            des.setCapability("marionette", true);
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        return driver;
    }

    public void setupDriverPaths() {
        System.setProperty(PROPERTYFIREFOX, GECKODRIVER_PATH);
        System.setProperty(PROPERTYCHROME, CHROMEDRIVER_PATH);

    }
}
