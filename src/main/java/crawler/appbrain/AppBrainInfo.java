package crawler.appbrain;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tante on 17/7/18. for AppBrain Information extracting
 */
public class AppBrainInfo {

    public static final String LAST_APP_BRAIN_CRAWL_TIMESTAMP = "lastAppBrainCrawlTimestamp";
    public static final String RANKING = "ranking";
    public static final String BINARY_SIZE = "binarySize";
    public static final String LIBRARIES = "libraries";
    public static final String AGE = "age";
    public static final String COMMENTS_TAG = "commentsTag";
    public static final String POSITIVE_COMMENT_TAG = "positiveCommentsTag";
    public static final String NEGATIVE_COMMENT_TAG = "negativeCommentsTag";
    public static final String RESOURCE_PERMISSIONS = "resourcePermissions";

    private String appId;

    private long lastAppBrainCrawlTimestamp;
    private String ranking;
    private String size;
    private int libraries;
    private String age;
    private Map<String, String> commentsTag;
    private Map<String, String> resourcePermissions;

    public AppBrainInfo(String appId){
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public long getLastAppBrainCrawlTimestamp() {
        return lastAppBrainCrawlTimestamp;
    }

    public void setLastAppBrainCrawlTimestamp(long lastAppBrainCrawlTimestamp) {
        this.lastAppBrainCrawlTimestamp = lastAppBrainCrawlTimestamp;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getLibraries() {
        return libraries;
    }

    public void setLibraries(int libraries) {
        this.libraries = libraries;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Map<String, String> getCommentsTag() {
        return commentsTag;
    }

    public List<BasicDBObject> getCommentsTagDocument() {
        List<BasicDBObject> comments = new ArrayList<BasicDBObject>();
        for (Map.Entry<String, String> entry : this.getCommentsTag().entrySet())
        {
            comments.add(new BasicDBObject(entry.getKey(), entry.getValue()));
        }

        return comments;
    }

    public void setCommentsTag(Map<String,String> commentsTag) {
        this.commentsTag = commentsTag;
    }

    public Map<String, String> getResourcePermissions() {
        return resourcePermissions;
    }

    public List<BasicDBObject> getResourcePermissionsDocument() {
        List<BasicDBObject> permissions = new ArrayList<BasicDBObject>();
        for (Map.Entry<String, String> entry : this.getResourcePermissions().entrySet())
        {
            permissions.add(new BasicDBObject(entry.getKey(), entry.getValue()));
        }
        return permissions;
    }

    public void setResourcePermissions(Map<String, String> resourcePermissions) {
        this.resourcePermissions = resourcePermissions;
    }

}
