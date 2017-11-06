package crawler.appinfo;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class that stores extended info about an app.
 */
public class AppInfo {
    public static final String APP_ID = "appId";
    public static final String LAST_APP_INFO_CRAWL_TIMESTAMP = "lastAppInfoCrawlTimestamp";
    public static final String NAME = "name";
    public static final String LINK_NAME = "linkName";
    public static final String PRICE = "price";
    public static final String STAR_RATING = "starRating";
    public static final String CATEGORY = "category";
    public static final String BADGE = "badge";
    public static final String AUTHOR = "author";
    public static final String TOTAL_NR_OF_REVIEWS = "totalNrOfReviews";
    public static final String REVIEWS_PER_STAR_RATING = "reviewsPerStarRating";
    public static final String DESCRIPTION = "description";
    public static final String WHATS_NEW = "whatsNew";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String SIZE = "size";
    public static final String INSTALLS = "installs";
    public static final String CURRENT_VERSION = "currentVersion";
    public static final String REQUIRED_ANDROID_VERSION = "requiredAndroidVersion";
    public static final String CONTENT_RATING = "contentRating";
    public static final String PERMISSIONS = "permissions";
    public static final String IN_APP_PRODUCTS = "inAppProducts";
    public static final String APP_URL = "appUrl";
    public static final String SIMILAR_APPS = "similarApps";
    public static final String USER_COMMENTS = "userComments";
    public static final String OFFERS_IN_APP_PURCHASES = "offersInAppPurchases";


    private static final int STARS = 5;
    private static final int PRICE_MULTIPLIER = 100;

    private final String name;
    private final String id;
    private final String linkName;
    private double starRating;
    private String category;
    private String badge;
    private String author;
    private long totalNrOfReviews;
    private long[] reviewsPerStars = new long[STARS];
    private String description;
    private String whatsNew;
    private double price;
    private String lastUpdated;
    private String size;
    private String installs;
    private String currentVersion;
    private String requiredAndroidVersion;
    private String contentRating;
    private String permissions;
    private String inApppProducts;
    private long lastAppInfoCrawlTimestamp;
    private String appUrl;
    private Set<String> similarApps;
    private String userComments;
    private String offersInAppPurchases;

    public AppInfo(String name, String linkName, String id) {
        this.name = name;
        this.linkName = linkName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLinkName() {
        return linkName;
    }

    public double getStarRating() {
        return starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadge() {
        return badge;
    }

    public void setAuthor(String company) {
        this.author = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setTotalNrOfReviews(long totalNrOfReviews) {
        this.totalNrOfReviews = totalNrOfReviews;
    }

    public long getTotalNrOfReviews() {
        return totalNrOfReviews;
    }

    public void setReviewsPerStars(long[] reviewsPerStars) {
        this.reviewsPerStars = reviewsPerStars;
    }

    public String getReviewsPerStarString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STARS; i++) {
            sb.append(i + 1).append(" stars: ").append(reviewsPerStars[i]).append(" ");
        }
        return sb.toString();
    }

    public List<BasicDBObject> getReviewsPerStarDocument() {
        List<BasicDBObject> starsRatings = new ArrayList<BasicDBObject>();
        for (int i = 0; i < STARS; i++) {
            starsRatings.add(new BasicDBObject(String.valueOf(i + 1), reviewsPerStars[i]));
        }
        return starsRatings;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setPrice(double price) {
        this.price = (int) (price * PRICE_MULTIPLIER);
    }

    public double getPrice() {
        return ((double) price) / PRICE_MULTIPLIER;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdates(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setInstalls(String installs) {
        this.installs = installs;
    }

    public String getInstalls() {
        return installs;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setRequiredAndroidVersion(String requiredAndroidVersion) {
        this.requiredAndroidVersion = requiredAndroidVersion;
    }

    public String getRequiredAndroidVersion() {
        return requiredAndroidVersion;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setInAppProducts(String inAppProducts) {
        this.inApppProducts = inAppProducts;
    }

    public String getInAppProducts() {
        return inApppProducts;
    }

    public long getLastAppInfoCrawlTimestamp() {
        return lastAppInfoCrawlTimestamp;
    }

    public void setLastAppInfoCrawlTimestamp(long lastAppInfoCrawlTimestamp) {
        this.lastAppInfoCrawlTimestamp = lastAppInfoCrawlTimestamp;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Set<String> getSimilarApps() {
        return similarApps;
    }

    public void setSimilarApps(Set<String> similarApps) {
        this.similarApps = similarApps;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String comments) {
        this.userComments = comments;
    }

    public String getOffersInAppPurchases() {
        return offersInAppPurchases;
    }

    public void setOffersInAppPurchases(String offersInAppPurchases) {
        this.offersInAppPurchases = offersInAppPurchases;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(APP_ID).append(": ").append(getId())
                .append("\n").append(LAST_APP_INFO_CRAWL_TIMESTAMP).append(": ").append(getLastAppInfoCrawlTimestamp())
                .append("\n").append(NAME).append(": ").append(getName())
                .append("\n").append(LINK_NAME).append(": ").append(getLinkName())
                .append("\n").append(PRICE).append(": ").append(getPrice())
                .append("\n").append(STAR_RATING).append(": ").append(getStarRating())
                .append("\n").append(CATEGORY).append(": ").append(getCategory())
                .append("\n").append(BADGE).append(": ").append(getBadge())
                .append("\n").append(AUTHOR).append(": ").append(getAuthor())
                .append("\n").append(TOTAL_NR_OF_REVIEWS).append(": ").append(getTotalNrOfReviews())
                .append("\n").append(REVIEWS_PER_STAR_RATING).append(": ").append(getReviewsPerStarString())
                .append("\n").append(DESCRIPTION).append(": ").append(getDescription())
                .append("\n").append(WHATS_NEW).append(": ").append(getWhatsNew())
                .append("\n").append(LAST_UPDATED).append(": ").append(getLastUpdated())
                .append("\n").append(SIZE).append(": ").append(getSize())
                .append("\n").append(INSTALLS).append(": ").append(getInstalls())
                .append("\n").append(CURRENT_VERSION).append(": ").append(getCurrentVersion())
                .append("\n").append(REQUIRED_ANDROID_VERSION).append(": ").append(getRequiredAndroidVersion())
                .append("\n").append(CONTENT_RATING).append(": ").append(getContentRating())
                .append("\n").append(PERMISSIONS).append(": ").append(getPermissions())
                .append("\n").append(IN_APP_PRODUCTS).append(": ").append(getInAppProducts())
                .append("\n").append(APP_URL).append(": ").append(getAppUrl())
                .append("\n").append(SIMILAR_APPS).append(": ").append(getSimilarApps())
                .append("\n").append(USER_COMMENTS).append(": ").append(getUserComments())
                .append("\n").append(OFFERS_IN_APP_PURCHASES).append(": ").append(getOffersInAppPurchases());
        return sb.toString();
    }

    public Document convertToDocument() {
        return new Document()
                .append(APP_ID, getId())
                .append(LAST_APP_INFO_CRAWL_TIMESTAMP, getLastAppInfoCrawlTimestamp())
                .append(NAME, getName())
                .append(LINK_NAME, getLinkName())
                .append(PRICE, getPrice())
                .append(STAR_RATING, getStarRating())
                .append(CATEGORY, getCategory())
                .append(BADGE, getBadge())
                .append(AUTHOR, getAuthor())
                .append(TOTAL_NR_OF_REVIEWS, getTotalNrOfReviews())
                .append(REVIEWS_PER_STAR_RATING, getReviewsPerStarDocument())
                .append(DESCRIPTION, getDescription())
                .append(WHATS_NEW, getWhatsNew())
                .append(LAST_UPDATED, getLastUpdated())
                .append(SIZE, getSize())
                .append(INSTALLS, getInstalls())
                .append(CURRENT_VERSION, getCurrentVersion())
                .append(REQUIRED_ANDROID_VERSION, getRequiredAndroidVersion())
                .append(CONTENT_RATING, getContentRating())
                .append(PERMISSIONS, getPermissions())
                .append(IN_APP_PRODUCTS, getInAppProducts())
                .append(APP_URL, getAppUrl())
                .append(SIMILAR_APPS, getSimilarApps())
                .append(USER_COMMENTS, getUserComments())
                .append(OFFERS_IN_APP_PURCHASES, getOffersInAppPurchases());
    }
}
