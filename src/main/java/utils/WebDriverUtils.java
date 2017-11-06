package utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.ConnectionClosedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;

/**
 * WebDriver utility methods.
 */
public abstract class WebDriverUtils {
    private static final Logger log = Logger.getLogger(WebDriverUtils.class.getName());
    private static final long DEFAULT_SLEEP_MILLIS = 2000;

    public static WebDriver driver;

    private static void goToUrl(String url) throws Exception {
        log.info("Navigating to " + url);

        try {
            driver.navigate().to(url);

        } catch (NoSuchSessionException | ConnectionClosedException e) {
            createDriver();
        }
    }

    public static void goToUrlWithWait(String appUrl, By forLocator) throws Exception {
        goToUrlWithWaitInSec(appUrl, forLocator, 10);
    }

    public static void goToUrlWithWaitInSec(String appUrl, By forLocator, long inSeconds) throws Exception {
        goToUrl(appUrl);
        WebDriverWait wait = new WebDriverWait(driver, inSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(forLocator));
    }

    public static void createDriver() throws MalformedURLException {
        String seleniumUrl = EnvReader.readEnvVariable("SELENIUM_URL", "localhost:4444");
        log.info("Connecting to Selenium at " + seleniumUrl);

        driver = new RemoteWebDriver(new URL("http://" + seleniumUrl + "/wd/hub"), DesiredCapabilities.chrome());
        driver.manage().window().maximize();

        // just a quick check
        driver.get("http://google.com");
        assertEquals("The page title should equal Google at the start of the test", "Google", driver.getTitle());
    }

    public static void scrollToElement(String className) {
        WebElement element = WebDriverUtils.driver.findElement(By.className(className));
        Actions actions = new Actions(WebDriverUtils.driver);
        actions.moveToElement(element);
    }

    public static void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        sleep();
    }

    public static boolean checkWithinView(WebElement element) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript("function elementInViewport(el) {\n" +
                "  var top = el.offsetTop;\n" +
                "  var left = el.offsetLeft;\n" +
                "  var width = el.offsetWidth;\n" +
                "  var height = el.offsetHeight;\n" +
                "\n" +
                "  while(el.offsetParent) {\n" +
                "    el = el.offsetParent;\n" +
                "    top += el.offsetTop;\n" +
                "    left += el.offsetLeft;\n" +
                "  }\n" +
                "\n" +
                "  return (\n" +
                "    top >= window.pageYOffset &&\n" +
                "    left >= window.pageXOffset &&\n" +
                "    (top + height) <= (window.pageYOffset + window.innerHeight) &&\n" +
                "    (left + width) <= (window.pageXOffset + window.innerWidth)\n" +
                "  );\n" +
                "}" +
                "return elementInViewport(arguments[0])" , element);
    }

    public static String getComputedStyleProperty(WebElement element, String property) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        return String.valueOf(executor.executeScript("return window.getComputedStyle(arguments[0], null).getPropertyValue(arguments[1])"
                , element, property));
    }

    public static void sleep() {
        sleep(DEFAULT_SLEEP_MILLIS);
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clickOnElementWithJs(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public static String cleanupText(String text, String[] startsWithStrings, String endsWith) {
        text = text.trim();
        for (String startsWith : startsWithStrings) {
            if (!Strings.isNullOrEmpty(startsWith) && text.toLowerCase().startsWith(startsWith.toLowerCase())) {
                text = text.substring(startsWith.length());
                text = text.trim();
                continue;
            }
        }
        if (!Strings.isNullOrEmpty(endsWith) && text.toLowerCase().endsWith(endsWith.toLowerCase())) {
            text = text.substring(0, text.length() - endsWith.length());
            text = text.trim();
        }
        return text;
    }

    public static String cleanupText(String text, String startsWith, String endsWith) {
        return cleanupText(text, new String[]{startsWith}, endsWith);
    }

    public static String extractElementsAttributeIfFound(By by, String attribute, String strIfNotFound) {
        try {
            return extractAttribute(driver.findElement(by), attribute, strIfNotFound);

        } catch (Exception e) {
            return strIfNotFound;
        }
    }

    public static String extractAttribute(WebElement element, String attribute, String str) {
        try {
            return trimText(element.getAttribute(attribute), str);

        } catch (Exception e) {
            return str;
        }
    }

    public static String extractElementContentIfFound(By by, String strIfNotFound) {
        try {
            return extractText(driver.findElement(by), strIfNotFound);

        } catch (Exception e) {
            return strIfNotFound;
        }
    }

    public static String extractText(WebElement element, String str) {
        return trimText(element.getText(), str);
    }

    public static String trimText(String toTrim, String str2) {
        String text = StringUtils.trimToNull(toTrim);
        return text == null ? str2 : text;
    }

    public static List<WebElement> getCardList() {
        List<WebElement> cards = driver.findElements(By.cssSelector("div.id-card-list.card-list>div.card"));
        log.info("Number of cards extracted: " + cards.size());
        return cards;
    }
}
