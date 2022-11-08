package test.functional;

import core.DriverHelper;
import io.github.sukgu.Shadow;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import test.po.CheckoutPO;
import test.po.DeviceProtectionPO;
import test.po.MasterPO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import static test.po.DeviceProtectionPO.selectedPriceText;

public class DeviceProtection {


    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static Shadow shadow;
    private WebDriverWait wait;

    /**
     * Setup drivers instance and thread
     * Create static shadow object
     *
     * @param browser
     */
    private void setup(String browser) {
        DriverHelper driverHelper = new DriverHelper();
        driver.set(driverHelper.setupDriver(browser));
        wait = new MasterPO(driver).getWait();
        this.shadow = new Shadow(driver.get());
        try {
            shadow.setExplicitWait(25, 3);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param browser
     * @param url
     * @throws Exception
     */
    @Parameters({"browser", "url"})
    @Test(description = "First Requirement test")
    public void firstRequirement(@Optional("chrome") String browser,
                                 @Optional("https://www.bolttech.co.th/en/device-protection") String url) throws Exception {
        setup(browser);
        driver.get().get(url);

        DeviceProtectionPO deviceProtectionPO = new DeviceProtectionPO(driver);
        Shadow shadow = new Shadow(driver.get());
        deviceProtectionPO.selectPurchasePrice(false, true);
        WebElement currentPrice = shadow.findElementByXPath("//edi-dropdown[@id='purchasePrice']");
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOf(currentPrice)).isDisplayed(), true, "Waited the dropdown and select the device price");
    }

    @Parameters({"browser", "url", "betweenPrice", "randomPrice"})
    @Test(description = "Second Requirement test")
    public void secondRequirement(@Optional("chrome") String browser,
                                  @Optional("https://www.bolttech.co.th/en/device-protection") String url,
                                  @Optional("") String betweenPrice,
                                  @Optional("true") boolean randomPrice) throws Exception {
        setup(browser);
        driver.get().get(url);
        Shadow shadow = new Shadow(driver.get());
        DeviceProtectionPO deviceProtectionPO = new DeviceProtectionPO(driver);
        deviceProtectionPO.selectPurchasePrice(true, randomPrice, betweenPrice);
        if (randomPrice) {
            Assert.assertEquals(shadow.findElementByXPath("//span[@id='selected']").getText(), selectedPriceText, "The selected price should be the current value for the Dropdown");
        } else {
            Assert.assertEquals(shadow.findElementByXPath("//span[@id='selected']").getText(), betweenPrice, "The selected price should be the current value for the Dropdown");
        }
    }

    @Parameters({"browser", "url", "randomPrice"})
    @Test(description = "Third Requirement test")
    public void thirdRequirement(@Optional("chrome") String browser,
                                 @Optional("https://www.bolttech.co.th/en/device-protection") String url,
                                 @Optional("true") boolean randomPrice) throws Exception {
        setup(browser);
        driver.get().get(url);
        DeviceProtectionPO deviceProtectionPO = new DeviceProtectionPO(driver);
        deviceProtectionPO.selectPurchasePrice(true, randomPrice).verifyTextUnderCard();
        Shadow shadow = new Shadow(driver.get());
        WebElement lblPriceCard = shadow.findElementByXPath("//span[@id='dynamicPrice']");
        Assert.assertEquals(wait.until(ExpectedConditions.textToBePresentInElement(lblPriceCard, deviceProtectionPO.listPriceRelations(selectedPriceText))).booleanValue(), true, "Text under Card correspond to the relation between price range and price/month");
        deviceProtectionPO.verifyTextUnderTable(selectedPriceText);
        WebElement lblPriceTable = shadow.findElementByXPath("//*[contains(text(),'" + deviceProtectionPO.listPriceRelations(selectedPriceText) + "')]");
        Assert.assertEquals(wait.until(ExpectedConditions.textToBePresentInElement(lblPriceTable, deviceProtectionPO.listPriceRelations(selectedPriceText))).booleanValue(), true, "Text under Table correspond to the relation between price range and price/month");
    }

    @Parameters({"browser", "url"})
    @Test(description = "Fourth Requirement test")
    public void fourthRequirement(@Optional("chrome") String browser,
                                  @Optional("https://www.bolttech.co.th/en/device-protection") String url) {
        setup(browser);
        driver.get().get(url);
        DeviceProtectionPO deviceProtectionPO = new DeviceProtectionPO(driver);
        deviceProtectionPO.goToMoreDetailsButton();
        WebElement parentModalContainer = shadow.findElementByXPath("//div[@class='edi-modal-dialog__body']");
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOf(parentModalContainer)).isDisplayed(), true, "Possible to see the modal");
        WebElement modalContainer = shadow.findElementByXPath("//img[@alt='Modal image']");
        String imgUrl = modalContainer.getAttribute("src");
        Assert.assertEquals(modalContainer.getAttribute("src"), "https://d12cimvifkzl84.cloudfront.net/assets/v2/1666357120_show_more_en.png", "Img inside the modal container");
        driver.get().switchTo().newWindow(WindowType.TAB);
        driver.get().get(imgUrl);
        Assert.assertEquals(imgUrl, driver.get().getCurrentUrl(), "Current Url is the same as image src propertie");
        deviceProtectionPO.closeTab().closeModal();

        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOf(shadow.findElementByXPath("//*[contains(text(),'Device purchase price')]"))).isDisplayed(), true, "The Device Purchase price is visible, the modal was closed");
    }

    @Parameters({"browser", "url"})
    @Test(description = "Fiveth and Sixth Requirement test")
    public void fivethAndSixthRequirements(@Optional("chrome") String browser,
                                           @Optional("https://www.bolttech.co.th/en/device-protection") String url){
        setup(browser);
        driver.get().get(url);
        WebElement containerCheckout = new DeviceProtectionPO(driver).acessPurchase().getContainerCheckoutPage();
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOf(containerCheckout)).isDisplayed(), true, "Load the checkoutPage");
        Assert.assertNotEquals(url, driver.get().getCurrentUrl());
    }

    @Parameters({"browser", "url"})
    @Test(description = "Seventh Requirement test")
    public void seventhRequirements(@Optional("chrome") String browser,
                                    @Optional("https://www.bolttech.co.th/en/device-protection") String url){
        setup(browser);
        driver.get().get(url);
        DeviceProtectionPO deviceProtectionPO = new DeviceProtectionPO(driver);
        String productName = deviceProtectionPO.returnProductName();
        CheckoutPO checkoutPO = new DeviceProtectionPO(driver).acessPurchase();
        WebElement lblPriceCard = shadow.findElementByXPath("//p[@class='original-price']");
        Assert.assertEquals(wait.until(ExpectedConditions.textToBePresentInElement(lblPriceCard, deviceProtectionPO.listPriceRelations(selectedPriceText))).booleanValue(), true, "Text under Card correspond to the relation between price range and price/month");
        Assert.assertEquals(productName, shadow.findElementByXPath("//p[contains(text(),'Screen Breakage')]").getText(), "The product name selected is the same in both pages");
        Assert.assertEquals(shadow.findElementByXPath("//p[@class='final-price']").getText(), "฿0.00", "Final price is 0 as the 2 months is for free");
        Assert.assertEquals(shadow.findElementByXPath("//span[@id='providerName']").getText(), "bolttech", "Provider is Bolttech");
        Assert.assertEquals(shadow.findElementByXPath("//span[@id='subscriptionRenewal']").getText(), "Monthly", "Contract Renewal is set to Monthly");
    }

    @AfterMethod
    private void tearDown() {
        try {
            File imagem = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(imagem, new File(Paths.get(Paths.get(System.getProperty("user.dir"), "target").toString(), "screenshot" + new Random().nextInt()).toString() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error trying to take screenshot...");
        }

        driver.get().quit();
    }
}
