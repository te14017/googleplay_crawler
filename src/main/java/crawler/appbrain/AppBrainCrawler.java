package crawler.appbrain;

import crawler.AbstractCrawler;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import utils.WebDriverUtils;

import java.util.*;
import java.util.logging.Logger;

import static crawler.appbrain.AppBrainInfo.*;
import static utils.WebDriverUtils.*;

/**
 * Crawler that collects information about apps from App Brain
 */
public class AppBrainCrawler extends AbstractCrawler {
    private static final Logger log = Logger.getLogger(AppBrainCrawler.class.getName());

    private static final String APPBRAIN_PREFIX = "https://www.appbrain.com/app/";

    private AppBrainCrawler() {
        super();
    }

    public static void main(String[] args) {
        AppBrainCrawler crawler = new AppBrainCrawler();
        crawler.run();
    }

    @Override
    public void crawl() {
        List<AppBrainInfo> appInfos = crawlAppBrainInfos(getAppIds());
        log.info("Crawled " + appInfos.size());
    }

    private List<AppBrainInfo> crawlAppBrainInfos(List<String> appIds) {
        List<AppBrainInfo> appBrainInfos = new ArrayList<AppBrainInfo>();

        // shuffle appIds to lower down risk of stuck in some particular app pages
        Collections.shuffle(appIds);

        for (String appId : appIds) {
            AppBrainInfo appBrainInfo = crawlAppBrainInfoForId(appId);
            if (appBrainInfo != null) {
                appBrainInfos.add(appBrainInfo);
                // immediatly write to avoid information loss when crashing
                dbWriter.writeAppBrainInfos(Collections.singletonList(appBrainInfo));
            }
        }
        return appBrainInfos;
    }

    private AppBrainInfo crawlAppBrainInfoForId(String appId) {
        String url = APPBRAIN_PREFIX + appId;

        try {
            // have a long wait so we get a built in timeout
            goToUrlWithWaitInSec(url, By.className("app-top-title"), 60);
            AppBrainInfo appBrainInfo = new AppBrainInfo(appId);
            appBrainInfo.setLastAppBrainCrawlTimestamp(System.currentTimeMillis());
            appBrainInfo.setAge(extractAge());
            appBrainInfo.setSize(extractSize());
            appBrainInfo.setCommentsTag(extractCommentsTag());
            appBrainInfo.setLibraries(extractLibraries());
            appBrainInfo.setRanking(extractRanking());
            appBrainInfo.setResourcePermissions(extractResourcePermissions());
            return appBrainInfo;
        } catch (TimeoutException e) {
            log.info("TimeoutException: Looking for app-top-title in url: " + url);
            // 5 min
            sleep(300000);
            return null;
        } catch (Exception e) {
            logException(e, url);
            return null;
        }
    }

    @Override
    public String timestampFieldName() {
        return LAST_APP_BRAIN_CRAWL_TIMESTAMP;
    }

    private String extractRanking() {
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.xpath("//a[@class='infotile ' and @title='Ranking']"));
        String rank = EMPTY;
        String subtext = EMPTY;
        if (elements.size() != 0) {
            rank = extractText(elements.get(0).findElement(By.className("infotile-text")), EMPTY);
            subtext = EMPTY;
            List<WebElement> subtextElements = elements.get(0).findElements(By.className("infotile-subtext"));
            if (subtextElements.size() != 0) {
                subtext = extractText(subtextElements.get(0), EMPTY);
            }
        }
//        System.out.println("Ranking extracted : " + rank + " " + subtext);
        return trimText(rank + " " + subtext, EMPTY);
    }

    private int extractLibraries() {
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.xpath("//a[@class='infotile ' and @title='Libraries']"));
        int num = 0;
        if (elements.size() != 0) {
            String libraries = extractText(elements.get(0).findElement(By.className("infotile-text")), EMPTY);
            try {
                num = Integer.parseInt(libraries);
            } catch (NumberFormatException e) {
                log.info("Skipping Libraries: " + e.getMessage());
                num = EMPTY_INT;
            }
        }
//        System.out.println("Libraries extracted : " + num);
        return num;
    }

    private String extractSize() {
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.xpath("//div[@class='infotile ' and @title='App size']"));
        String size = EMPTY;
        if (elements.size() != 0) {
            size = extractText(elements.get(0).findElement(By.className("infotile-text")), EMPTY);
        }
//        System.out.println("App size extracted : " + size);
        return size;
    }

    private String extractAge() {
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.xpath("//div[@class='infotile ' and @title='App Age']"));
        String text = EMPTY;
        String subtext = EMPTY;
        if (elements.size() != 0) {
            WebElement element = elements.get(0);
            text = extractText(element.findElement(By.className("infotile-text")), EMPTY);
            subtext = EMPTY;
            List<WebElement> subtextElements = element.findElements(By.className("infotile-subtext"));
            if (subtextElements.size() != 0) {
                subtext = extractText(subtextElements.get(0), EMPTY);
            }
        }
//        System.out.println("App Age extracted : " + text + " " + subtext);
        return trimText(text + " " + subtext, EMPTY);
    }

    private Map<String,String> extractResourcePermissions() {
        Map<String,String> resources = new HashMap<String, String>();
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.className("app-permissions"));
        if (elements.size() != 0) {
            WebElement element = elements.get(0);
            List<WebElement> subElements = element.findElements(By.cssSelector("div.default-box-color.vpadding-xs.hpadding-s"));

            for (WebElement element1 : subElements) {
                String resourceType = extractText(element1.findElement(By.xpath("b")), EMPTY);
                String resourceDescription = extractText(element1.findElement(By.xpath("div")), EMPTY);
                resources.put(resourceType, resourceDescription);
            }
        }

//        System.out.println("resource permissions extracted : " + resources);
        return resources;
    }

    private Map<String, String> extractCommentsTag() {
        Map<String,String> commentsTag = new HashMap<String, String>();
        List<WebElement> elements = WebDriverUtils.driver.findElements(By.className("col-sm-4"));

        // extract tags from Tag cloud, positive Tag cloud and negative Tag cloud
        for (WebElement element : elements) {
            String tagName = extractText(element.findElement(By.xpath("h3")), EMPTY);
            if (tagName.equals("Tag cloud") || tagName.equals("Positive comment tag cloud") || tagName.equals("Negative comment tag cloud")) {
                List<WebElement> terms = element.findElements(By.className("tag-cloud-term"));
                String tagString = EMPTY;
                for (WebElement term : terms) {
                    String termText = extractText(term, EMPTY);
                    tagString += termText + " ";
                }
                if (tagName.equals("Tag cloud")) {
                    commentsTag.put(COMMENTS_TAG, tagString.replace(COMMA, EMPTY));
                }
                if (tagName.equals("Positive comment tag cloud")) {
                    commentsTag.put(POSITIVE_COMMENT_TAG, tagString.replace(COMMA, EMPTY));
                }
                if (tagName.equals("Negative comment tag cloud")) {
                    commentsTag.put(NEGATIVE_COMMENT_TAG, tagString.replace(COMMA, EMPTY));
                }
            }
        }

//        System.out.println("comment tags extracted : " + commentsTag);
        return commentsTag;
    }

}
