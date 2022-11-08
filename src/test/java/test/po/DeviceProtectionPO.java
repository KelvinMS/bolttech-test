package test.po;

import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;

public class DeviceProtectionPO extends MasterPO{

    public static String selectedPriceText;
    private static Shadow shadow;

    public DeviceProtectionPO(ThreadLocal<WebDriver> driver) {

        super(driver);
        driver.get().findElement(By.xpath("//*[contains(text(),'Accept All Cookies')]")).click();
        try {
            shadow = new Shadow(driver.get());
            shadow.setExplicitWait(25, 3);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public DeviceProtectionPO selectPurchasePrice(boolean especificPrice, boolean random, String... devicePrice) throws InterruptedException {
        WebElement priceDropDownButton = shadow.findElementByXPath("//*[contains(text(),'Device purchase price')]");
        wait.until(ExpectedConditions.visibilityOf(priceDropDownButton)).click();
        if (especificPrice) {
            if (random) {
                List<WebElement> priceButtonElements = shadow.findElementsByXPath("//ul[@id='list']/child::li");
                Integer randomElementNumber = new Random().nextInt(priceButtonElements.size());
                selectedPriceText = priceButtonElements.get(randomElementNumber).getText();
                wait.until(ExpectedConditions.elementToBeClickable(priceButtonElements.get(randomElementNumber))).click();
            } else {
                WebElement priceButton = shadow.findElementByXPath("//li[contains(text(),'" + devicePrice[0] + "')]");
                wait.until(ExpectedConditions.visibilityOf(priceButton)).click();
            }
        } else {
            String currentPrice = shadow.findElementByXPath("//span[@id='selected']").getText();
            wait.until(ExpectedConditions.elementToBeClickable(shadow.findElementByXPath("//li[contains(text(),'" + currentPrice + "')]"))).click();
        }
        return this;
    }

    public CheckoutPO acessPurchase() {
        wait.until(ExpectedConditions.visibilityOf(shadow.findElementByXPath("//*[contains(text(),'Device purchase price')]")));
        selectedPriceText = shadow.findElementByXPath("//span[@id='selected']").getText();
        WebElement btnSelect = shadow.findElementByXPath("//span[contains(text(),'Select')]");
        if (!btnSelect.isDisplayed()){
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSelect);
        }
        wait.until(ExpectedConditions.elementToBeClickable(btnSelect));
        btnSelect.click();
        return new CheckoutPO(driver);
    }

    public String returnProductName(){
        return shadow.findElementByXPath("//p[@class='card-title']").getText();

    }

    public DeviceProtectionPO verifyTextUnderCard() {
        Actions actions = new Actions(driver.get());
        WebElement cardTextPrice = shadow.findElementByXPath("//span[@id='dynamicPrice']");
        actions.moveToElement(cardTextPrice);
        return this;
    }

    public DeviceProtectionPO verifyTextUnderTable(String priceRange){
        Actions actions = new Actions(driver.get());
        WebElement cardTextPrice = shadow.findElementByXPath("//div[contains(text(),'Price per month')]");
        actions.moveToElement(cardTextPrice).build().perform();
        return this;
    }

    public DeviceProtectionPO goToMoreDetailsButton() {
        WebElement lblSpeedRepair = shadow.findElementByXPath("//div[contains(text(),'Speed of repair (Other Metros)')]");
        ((JavascriptExecutor) driver.get()).executeScript("arguments[0].scrollIntoView({block: 'center'});", lblSpeedRepair);
        wait.until(ExpectedConditions.visibilityOf(lblSpeedRepair)).isDisplayed();
        WebElement btnMoreDetails = shadow.findElementByXPath("//a[contains(text(),'more details')]");
        wait.until(ExpectedConditions.elementToBeClickable(btnMoreDetails));
        Actions actions = new Actions(driver.get());
        actions.click(btnMoreDetails).build().perform();
        return this;
    }

    public DeviceProtectionPO closeTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> abas = new ArrayList<String>(driver.get().getWindowHandles());
        driver.get().switchTo().window(abas.get(1));
        driver.get().close();
        driver.get().switchTo().window(abas.get(0));
        return this;
    }

    public DeviceProtectionPO closeModal() {
        shadow.findElementByXPath("//button[@class='edi-modal-dialog__close']").click();
        return this;
    }

    public String listPriceRelations(String range) {
        Map<String, String> prices = new HashMap<String, String>();
        prices.put("THB 2,000 - 6,000", "฿39.00");
        prices.put("THB 6,000 - 10,000", "฿59.00");
        prices.put("THB 10,001 - 15,000", "฿79.00");
        prices.put("THB 15,001 - 22,000", "฿139.00");
        prices.put("THB 26,001 - 36,000", "฿179.00");
        prices.put("THB 36,001 - 65,000", "฿289.00");

        return prices.get(range);
    }


}
