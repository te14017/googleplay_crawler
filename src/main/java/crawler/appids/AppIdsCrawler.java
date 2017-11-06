package crawler.appids;

import crawler.AbstractCrawler;
import org.apache.maven.shared.utils.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import utils.WebDriverUtils;

import java.util.*;
import java.util.logging.Logger;

import static utils.WebDriverUtils.*;

/**
 * Crawler that collects information about apps that are specified in a .csv file.
 */
public class AppIdsCrawler extends AbstractCrawler {
    private static final Logger log = Logger.getLogger(AppIdsCrawler.class.getName());

    private static final List<String> urls = new ArrayList<String>(60);
    
    private AppIdsCrawler() {
        super();

        // categories
        urls.add("http://play.google.com/store/apps/category/ART_AND_DESIGN/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/AUTO_AND_VEHICLES/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/BEAUTY/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/ANDROID_WEAR/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/BOOKS_AND_REFERENCE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/BUSINESS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/COMICS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/COMMUNICATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/DATING/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/EDUCATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/ENTERTAINMENT/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/EVENTS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FINANCE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FOOD_AND_DRINK/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/HEALTH_AND_FITNESS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/HOUSE_AND_HOME/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/LIBRARIES_AND_DEMO/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/LIFESTYLE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/MAPS_AND_NAVIGATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/MEDICAL/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/MUSIC_AND_AUDIO/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/NEWS_AND_MAGAZINES/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/PARENTING/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/PERSONALIZATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/PHOTOGRAPHY/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/PRODUCTIVITY/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/SHOPPING/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/SOCIAL/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/SPORTS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/TOOLS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/TRAVEL_AND_LOCAL/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/VIDEO_PLAYERS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/WEATHER/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_ACTION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_ADVENTURE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_ARCADE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_BOARD/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_CARD/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_CASINO/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_CASUAL/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_EDUCATIONAL/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_MUSIC/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_PUZZLE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_RACING/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_ROLE_PLAYING/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_SIMULATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_SPORTS/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_STRATEGY/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_TRIVIA/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/GAME_WORD/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_ACTION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_BRAINGAMES/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_CREATE/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_EDUCATION/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_MUSICVIDEO/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
        urls.add("http://play.google.com/store/apps/category/FAMILY_PRETEND/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);

        // top paid overall
        urls.add("https://play.google.com/store/apps/collection/topselling_paid" + "?" + PLAY_STORE_ATTR);
    }

    public static void main(String[] args) {
        AppIdsCrawler crawler = new AppIdsCrawler();
        crawler.run();
    }

    @Override
    public void crawl() {
        Set<String> ids = crawlAppIds();
        log.info("Crawled " + ids.size());
    }

    @Override
    public String timestampFieldName() {
        log.info("You're doing it wrong.");
        return null;
    }

    private Set<String> crawlAppIds() {
        Set<String> ids = new HashSet<String>();

        // shuffle to not show a pattern
        Collections.shuffle(urls);

        for (String url :  urls) {
            Set<String> idsForUrl = crawlAppIdsForUrl(url);
            ids.addAll(idsForUrl);
            // immediatly write to avoid information loss when crashing
            dbWriter.writeAppIdsToDb(idsForUrl);
        }

        return ids;
    }

    private Set<String> crawlAppIdsForUrl(String url) {
        // result
        Set<String> ids = new HashSet<String>();

        try {
            WebDriverUtils.goToUrlWithWait(url, By.className("id-card-list"));
        } catch (TimeoutException e) {
            log.info("TimeoutException: Looking for id-card-list in url: " + url);
            return ids;
        } catch (Exception e) {
            logException(e, url);
            return ids;
        }

        WebElement showMoreButton = WebDriverUtils.driver.findElement(By.id("show-more-button"));
        WebElement footer = WebDriverUtils.driver.findElement(By.className("footer-link"));

        int blockFlag = 0;
        while (blockFlag == 0 || !getComputedStyleProperty(showMoreButton, "display").equals("none")) {
            WebDriverUtils.scrollDown();
//            System.out.print(getComputedStyleProperty(driver, showMoreButton, "display") + "\n");
            if(getComputedStyleProperty(showMoreButton, "display").equals("block")) {
                blockFlag = 1;
                WebDriverUtils.scrollDown();
            }

            if (showMoreButton.isDisplayed()) {
                clickOnElementWithJs(showMoreButton);
            }

            // check if footer within screen view
            if (checkWithinView(footer)) {
                // if show-more button shown, click it. if no more apps after click, then skip it
                if (getComputedStyleProperty(showMoreButton, "display").equals("block")) {
                    clickOnElementWithJs(showMoreButton);
                    sleep();
                    if (checkWithinView(footer)) {
                        break;
                    }
                } else if (getComputedStyleProperty(showMoreButton, "display").equals("none")) {
                    break;
                }
            }
        }

        // make sure scroll down to bottom
        while(!checkWithinView(footer)) {
            WebDriverUtils.scrollDown();
        }

        // collect ids
        for (WebElement element: WebDriverUtils.getCardList()) {
            String id = extractAttribute(element, "data-docid", EMPTY);

            if (StringUtils.isNotEmpty(id)) {
                ids.add(id);
            }
        }

        log.info("Number of ids extracted: " + ids.size());
        return ids;
    }

}
