package test.po;

import io.github.sukgu.Shadow;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPO extends MasterPO{

    public CheckoutPO(ThreadLocal< WebDriver > driver) {
        super(driver);
    }

    private static Shadow shadow;

    public WebElement getContainerCheckoutPage() {
        this.shadow = new Shadow(driver.get());
        try {
            shadow.setExplicitWait(25, 3);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        WebElement containerCheckout = shadow.findElementByXPath("//div[@class='product-summary-box']");
        wait.until(ExpectedConditions.visibilityOf(containerCheckout));
        return containerCheckout;
    }


}
