package db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.IndexOptions;
import crawler.appbrain.AppBrainInfo;
import crawler.appinfo.AppInfo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.maven.shared.utils.StringUtils;
import org.bson.Document;

import java.util.*;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static crawler.appbrain.AppBrainInfo.*;
import static crawler.appinfo.AppInfo.*;

public class DBWriter {

    private static final Logger log = Logger.getLogger(DBWriter.class.getName());

    private MongoDatabase db;
    private String databaseName;
    private MongoCollection mongoCollection;

    public DBWriter(String host, int port, String databaseName, String collectionName) {
        this.databaseName = databaseName;

        MongoClient mongoClient = new MongoClient(host, port);
        db = mongoClient.getDatabase(this.databaseName);
        mongoCollection = db.getCollection(collectionName);
        mongoCollection.createIndex(new BasicDBObject(APP_ID, 1), new IndexOptions().unique(true));
    };

    public void writeAppInfosToDb(List<AppInfo> appInfos) {
        int updates = 0;
        int inserts = 0;

        for (AppInfo appInfo : appInfos) {
            String appInfoId = appInfo.getId();
            Document appInfoDocument = appInfo.convertToDocument();
            FindIterable<Document> iterable = mongoCollection.find(new Document(APP_ID, appInfoId));
            Document first = iterable.first();
            if (first != null) {
                mongoCollection.updateOne(eq(APP_ID, appInfoId), new Document("$set", appInfoDocument));
                updates += 1;
            } else {
                mongoCollection.insertOne(appInfoDocument);
                inserts += 1;
            }
        }
        log.info("Counts Total: " + appInfos.size() + "\tUpdates: " + updates + "\tInserts: " + inserts);
    }

    public void writeAppIdsToDb(Set<String> ids) {
        int inserts = 0;

        for (String id : ids) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            Document appId = new Document().append(APP_ID, id);
            FindIterable<Document> iterable = mongoCollection.find(new Document(APP_ID, id));
            Document first = iterable.first();
            if (first == null) {
                mongoCollection.insertOne(appId);
                inserts += 1;
            }
        }
        log.info("Counts Total: " + ids.size() + "\tInserts: " + inserts);
    }

    public FindIterable<Document> readAppIds(String toInclude) {
        return mongoCollection.find().projection(fields(include(APP_ID, toInclude)));
    }

    public void writeAppBrainInfos(List<AppBrainInfo> appBrainInfos) {
        int updates = 0;

        for (AppBrainInfo appBrainInfo : appBrainInfos) {
            String appId = appBrainInfo.getAppId();
            FindIterable<Document> iterable = mongoCollection.find(new Document(APP_ID, appId));
            Document first = iterable.first();
            if (first != null) {
                first.put(LAST_APP_BRAIN_CRAWL_TIMESTAMP, appBrainInfo.getLastAppBrainCrawlTimestamp());
                first.put(RANKING, appBrainInfo.getRanking());
                first.put(BINARY_SIZE, appBrainInfo.getSize());
                first.put(LIBRARIES, appBrainInfo.getLibraries());
                first.put(AGE, appBrainInfo.getAge());
                first.put(COMMENTS_TAG, appBrainInfo.getCommentsTagDocument());
                first.put(RESOURCE_PERMISSIONS, appBrainInfo.getResourcePermissionsDocument());

                mongoCollection.updateOne(eq(APP_ID, appId), new Document("$set", first));
                updates += 1;
            }
        }
        log.info("Counts Total: " + appBrainInfos.size() + "\tUpdates: " + updates);
    }

}
