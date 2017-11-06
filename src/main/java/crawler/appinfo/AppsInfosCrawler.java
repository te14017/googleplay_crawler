package crawler.appinfo;

import crawler.AbstractCrawler;
import org.apache.maven.shared.utils.StringUtils;
import org.openqa.selenium.*;

import java.util.*;
import java.util.logging.Logger;

import static crawler.appinfo.AppInfo.LAST_APP_INFO_CRAWL_TIMESTAMP;
import static utils.WebDriverUtils.*;

/**
 * WebDriver crawler that crawls the extended apps info for a given url.
 */
public class AppsInfosCrawler extends AbstractCrawler{
    private static final Logger log = Logger.getLogger(AppsInfosCrawler.class.getName());

    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?" + PLAY_STORE_ATTR + "&id=";
    private static final String INSTALL = "install";

    private AppsInfosCrawler() {
        super();
    }

    public static void main(String[] args) {
        AppsInfosCrawler crawler = new AppsInfosCrawler();
        crawler.run();
    }

    @Override
    public void crawl() {
        List<AppInfo> appInfos = crawlAppInfos(getAppIds());
        log.info("Crawled " + appInfos.size());
    }

    private List<AppInfo> crawlAppInfos(List<String> appIds) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();

        // shuffle appIds to lower down risk of stuck in some particular app pages
        Collections.shuffle(appIds);

        for (String appId : appIds) {
            AppInfo appInfo = crawlAppInfoForUrl(PLAY_STORE_URL + appId);
            if (appInfo != null) {
                appInfos.add(appInfo);
                // immediatly write to avoid information loss when crashing
                dbWriter.writeAppInfosToDb(Collections.singletonList(appInfo));
            }
        }
        return appInfos;
    }

    private AppInfo crawlAppInfoForUrl(String appUrl) {
        try {
            goToUrlWithWait(appUrl, By.className("id-app-title"));
            return extractAppInfo(appUrl);

        } catch (TimeoutException e) {
            log.info("TimeoutException: Looking for id-app-title in url: " + appUrl);
        } catch (Exception e) {
            logException(e, appUrl);
        }
        return null;
    }

    @Override
    public String timestampFieldName() {
        return LAST_APP_INFO_CRAWL_TIMESTAMP;
    }

    private AppInfo extractAppInfo(String appUrl) {
        String googleAppName = extractGoogleAppName(appUrl);
        AppInfo appInfo = new AppInfo(extractAppTitle(), googleAppName, googleAppName);
        appInfo.setLastAppInfoCrawlTimestamp(System.currentTimeMillis());
        appInfo.setPrice(extractPrice());
        appInfo.setStarRating(extractStarRating());
        appInfo.setBadge(extractBadge());
        appInfo.setAuthor(extractAuthor());
        appInfo.setCategory(extractCategory());
        appInfo.setOffersInAppPurchases(extractOffersInAppPurchases());
        appInfo.setTotalNrOfReviews(extractTotalNrOfReviews());
        appInfo.setReviewsPerStars(extractReviewsPerStar());
        appInfo.setDescription(extractDescription());
        appInfo.setWhatsNew(extractWhatsNew());
        appInfo.setUserComments(extractUserComments());
        extractAdditionalInfo(appInfo);
        appInfo.setAppUrl(appUrl);
        return appInfo;
    }

    private void extractAdditionalInfo(AppInfo appInfo) {
        scrollToBottomOfPage();
        appInfo.setLastUpdates(extractLastUpdated());
        appInfo.setSize(extractSize());
        appInfo.setInstalls(extractInstalls());
        appInfo.setCurrentVersion(extractCurrentVersion());
        appInfo.setRequiredAndroidVersion(extractRequiredAndroidVersion());
        appInfo.setContentRating(extractContentRating());
        appInfo.setPermissions(extractPermissions());
        appInfo.setInAppProducts(extractInAppProducts());
        appInfo.setSimilarApps(extractSimilarApps());
    }

    private void scrollToBottomOfPage() {
        scrollToElement("footer-link");
        sleep();
    }

    private String extractLastUpdated() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='datePublished']"), EMPTY);
    }

    private String extractSize() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='fileSize']"), EMPTY);
    }

    private String extractInstalls() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='numDownloads']"), EMPTY);
    }

    private String extractCurrentVersion() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='softwareVersion']"), EMPTY);
    }

    private String extractRequiredAndroidVersion() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='operatingSystems']"), EMPTY);
    }

    private String extractContentRating() {
        return extractElementContentIfFound(By.cssSelector("div[itemprop='contentRating']"), EMPTY);
    }

    private String extractPermissions() {
        try {
            clickOnElementWithJs(driver.findElement(By.className("id-view-permissions-details")));
            sleep();

            String permissionsDetailsString = extractElementContentIfFound(By.className("id-permission-buckets"), EMPTY);

            clickOnElementWithJs(driver.findElement(By.id("close-dialog-button")));
            sleep();

            return permissionsDetailsString;
        } catch (Exception e) {
            log.info("Skipping Permissions: " + e.getMessage());
            return EMPTY;
        }
    }

    private String extractInAppProducts() {
        try {
            List<WebElement> metaInfos = driver.findElements(By.className("meta-info"));

            for (WebElement metaInfo : metaInfos) {
                String title = extractText(metaInfo.findElement(By.className("title")), EMPTY);

                if (StringUtils.equalsIgnoreCase("In-app Products", title)) {
                    return extractText(metaInfo.findElement(By.className("content")), EMPTY);
                }
            }
        } catch (Exception e) {
            log.info("Skipping In-App Products: " + e.getMessage());
        }
        return EMPTY;
    }

    private double extractPrice() {
        String priceContent = extractElementsAttributeIfFound(By.cssSelector("meta[itemprop='price']"), "Content", EMPTY);

        if (priceContent.contains(INSTALL)) {
            return 0;
        }

        // Price text is of the form $2.49
        try {
            return Double.parseDouble(priceContent.substring(1).replace(COMMA, EMPTY));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            log.info("Skipping Price: " + e.getMessage());
            return EMPTY_DOUBLE;
        }
    }

    private String extractGoogleAppName(String appUrl) {
        int index = appUrl.indexOf("id=");
        return trimText(appUrl.substring(index + 3), EMPTY);
    }

    private String extractAppTitle() {
        return extractElementContentIfFound(By.className("id-app-title"), EMPTY);
    }

    private double extractStarRating() {
        try {
            return Double.parseDouble(extractElementContentIfFound(By.className("score"), EMPTY));
        } catch (NumberFormatException e) {
            log.info("Skipping StarRating: " + e.getMessage());
            return EMPTY_DOUBLE;
        }
    }

    private String extractBadge() {
        return extractElementContentIfFound(By.className("badge-title"), EMPTY);
    }

    private String extractAuthor() {
        return extractElementContentIfFound(By.className("primary"), EMPTY);
    }

    private String extractCategory() {
        return extractElementContentIfFound(By.className("category"), EMPTY);
    }

    private long extractTotalNrOfReviews() {
        try {
            return Long.parseLong(extractElementContentIfFound(By.className("rating-count"), EMPTY).replace(COMMA, EMPTY));
        } catch (NumberFormatException e) {
            log.info("Skipping TotalNrOfReviews: " + e.getMessage());
            return EMPTY_INT;
        }
    }

    private long[] extractReviewsPerStar() {
        String[] classNames = {"one", "two", "three", "four", "five"};
        long[] reviewsPerStar = new long[5];

        for (int i = 0; i < reviewsPerStar.length; i++) {
            String className = classNames[i];
            String text = extractElementContentIfFound(By.cssSelector("." + className + ">.bar-number"), EMPTY).replace(COMMA, EMPTY);

            try {
                reviewsPerStar[i] =  Long.parseLong(text);
            } catch (NumberFormatException e) {
                log.info("Skipping ReviewsPerStar " + className + ": " + e.getMessage());
                reviewsPerStar[i] = EMPTY_INT;
            }
        }

        return reviewsPerStar;
    }

    private String extractDescription() {
        return cleanupText(extractElementContentIfFound(By.className("description"), EMPTY), EMPTY, "Read more");
    }

    private String extractWhatsNew() {
        try {
            scrollToElement("whatsnew");
            return cleanupText(extractElementContentIfFound(By.className("whatsnew"), EMPTY), new String[]{"What's New", "Whats new"}, "Read more");
        } catch (Exception e) {
            log.info("Skipping What's New: " + e.getMessage());
            return EMPTY;
        }
    }

    private Set<String> extractSimilarApps() {
        // result
        Set<String> ids = new HashSet<String>();
        String url = extractElementsAttributeIfFound(By.xpath("//a[contains(text(), 'Similar')]"), "href", EMPTY);

        if (StringUtils.isEmpty(url)) {
            log.info("Skipping SimilarApps: Got EMPTY url");
            return ids;
        }

        url += "&";
        url += PLAY_STORE_ATTR;

        try {
            goToUrlWithWait(url, By.className("id-card-list"));

        } catch (TimeoutException e) {
            log.info("TimeoutException: Looking for id-card-list in url: " + url);
            return ids;
        } catch (Exception e) {
            logException(e, url);
            return ids;
        }

        WebElement showMoreButton = driver.findElement(By.id("show-more-button"));
        WebElement footer = driver.findElement(By.className("footer-link"));

        while (!footer.isDisplayed()) {
            scrollDown();
            if (!getComputedStyleProperty(showMoreButton, "display").equals("none")) {
                clickOnElementWithJs(showMoreButton);
            }
        }

        // collect ids
        for (WebElement element : getCardList()) {
            String id = extractAttribute(element, "data-docid", EMPTY);
            String price = extractText(element.findElement(By.cssSelector("div.reason-set span.display-price")), EMPTY);

            if (StringUtils.isNotEmpty(id) && !StringUtils.equalsIgnoreCase(price, "FREE")) {
                ids.add(id);
            }
        }

        log.info("Number of ids extracted: " + ids.size());
        return ids;
    }

    private String extractUserComments() {
        try {
            scrollDown();
            List<WebElement> elements = driver.findElements(By.xpath("//div[@class='review-text']"));

            StringBuilder comments = new StringBuilder();
            for (WebElement element : elements) {
                comments.append(extractText(element, EMPTY)).append(SPACE);
            }

            return comments.toString();
        } catch (Exception e) {
            log.info("Skipping User Comments: " + e.getMessage());
            return EMPTY;
        }
    }

    private String extractOffersInAppPurchases() {
        return extractElementContentIfFound(By.className("inapp-msg"), EMPTY);
    }

}
