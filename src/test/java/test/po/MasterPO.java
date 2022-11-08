package test.po;

import io.github.sukgu.support.ElementFieldDecorator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MasterPO {


    final ThreadLocal<WebDriver> driver;
    final WebDriverWait wait;

    /**
     * Setup driver timeouts and waitting init elements
     * @param driver
     */
    public MasterPO(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
        ElementFieldDecorator decorator = new ElementFieldDecorator(new DefaultElementLocatorFactory(driver.get()));
        PageFactory.initElements(decorator, this);
        wait = new WebDriverWait(this.driver.get(), Duration.ofSeconds(20));
        waitDOMLoad();

    }

    /**
     * Waite the DOM document to be ready
     */
    public void waitDOMLoad() {
        ExpectedCondition<Boolean> onPageLoaded = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        wait.until(onPageLoaded);
    }

    public WebDriverWait getWait() {
        return wait;
    }
}
