package crawler;

import com.mongodb.client.FindIterable;
import db.DBWriter;
import org.bson.Document;
import utils.EnvReader;
import utils.WebDriverUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static crawler.appinfo.AppInfo.APP_ID;

/**
 * Abstract Crawler for all Crawlers
 */
public abstract class AbstractCrawler {

    private static final String DB_NAME = "apps";
    private static final String DATA_COLLECTION = "data";
    // 604800000 - one week
    private static final long THRESHOLD = new Long("2419200000"); // one month

    protected static final String PLAY_STORE_ATTR = "gl=us&hl=en";
    protected static final String COMMA = ",";
    protected static final String EMPTY = "";
    protected static final String SPACE = " ";
    protected static final int EMPTY_INT = 0;
    protected static final double EMPTY_DOUBLE = 0.0;

    private static final Logger log = Logger.getLogger(AbstractCrawler.class.getName());

    protected DBWriter dbWriter;

    public AbstractCrawler() {
        connectDriver();
        connectDB();
    }

    private void connectDB() {
        String mongoAppDataHost = EnvReader.readEnvVariable("MONGO_APP_DATA_HOST", "localhost");
        int mongoAppDataPort = Integer.getInteger(EnvReader.readEnvVariable("MONGO_APP_DATA_PORT"), 27017);

        log.info("Connecting to MongoDB at " + mongoAppDataHost + ":" + mongoAppDataPort);

        dbWriter = new DBWriter(mongoAppDataHost, mongoAppDataPort, DB_NAME, DATA_COLLECTION);
    }

    private void connectDriver() {
        try {
            WebDriverUtils.createDriver();
        } catch (MalformedURLException e) {
            logException(e, "Check env variables");
        }
    }

    public void run() {
        crawl();
        finish();
    }

    public abstract void crawl();

    private void finish() {
        log.info("Quitting the driver.");
        WebDriverUtils.driver.quit();
    }

    public abstract String timestampFieldName();

    protected List<String> getAppIds() {
        FindIterable<Document> docs = dbWriter.readAppIds(timestampFieldName());
        List<String> ids = new ArrayList<String>();

        for (Document doc : docs) {
            Long ts = doc.getLong(timestampFieldName());
            //if (ts == null || isOutdated(ts)) {
            if (ts == null) {
                ids.add(doc.getString(APP_ID));
            }
        }

        log.info("Number of ids fetched for update: " + ids.size());
        return ids;
    }

    private boolean isOutdated(Long ts) {
        return (System.currentTimeMillis() - ts) > THRESHOLD;
    }

    protected void logException(Exception e, String msg) {
        log.info("Exception occured: " + msg);
        log.info("Exception message: " + e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log.info("Exception StackTrace: " + sw.toString());
    }
}
