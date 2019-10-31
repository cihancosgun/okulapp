/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.data;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import com.okulapp.crud.dao.CrudListResult;

/**
 *
 * @author cihan
 */
public class MongoDataAdapter implements DataAdapter {

    public static final String _ID = "_id";
    public static final String NOT_SUPPORTED_FOR_SLAVE_SERVER = "Not supported for slave server.";
    private static final String BATCH_SIZE = "batchSize";
    private static final String COLLECTION = "collection";
    private static final String MORE = "getMore";
    private static final String FIRST_BATCH = "firstBatch";
    private static final String ID = "id";
    private static final String CURSOR = "cursor";
    private static final String NEXT_BATCH = "nextBatch";

    private String mongoHost = "";
    private int mongoPort = 27017;
    private boolean isSlaveServer = false;
    private MongoClient client;

    public MongoDataAdapter() {
    }

    @Override
    public void connect() {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        if (isSlaveServer) {
            builder.readPreference(ReadPreference.secondary());
        }
        if (mongoHost != null && !mongoHost.isEmpty() && client == null) {
            builder.socketTimeout(300000);
            builder.serverSelectionTimeout(300000);
            client = new MongoClient(new ServerAddress(mongoHost, mongoPort), builder.build());
        }
        if (client.isLocked()) {
            client.unlock();
        }
    }

    @Override
    public void disconnect() {
        if (client != null) {
            //client.close();
        }
    }

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public boolean isIsSlaveServer() {
        return isSlaveServer;
    }

    public void setIsSlaveServer(boolean isSlaveServer) {
        this.isSlaveServer = isSlaveServer;
    }

    @Override
    public CrudListResult getList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection) {
        this.connect();
        CrudListResult result = new CrudListResult();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        if (filter == null) {
            filter = new HashMap();
        }
        if (projection == null) {
            projection = new HashMap();
        }
        Document dp = new Document(projection);
        Document df = new Document(filter);
        MongoCursor<Document> cursor = collection.find(df).projection(dp).iterator();
        while (cursor.hasNext()) {
            Document next = cursor.next();
            result.addToList(next);
        }
        result.setTotalRecordCount(collection.count(df));
        this.disconnect();
        return result;
    }

    @Override
    public CrudListResult getSortedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, Map<String, Object> sort) {
        this.connect();
        CrudListResult result = new CrudListResult();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        if (filter == null) {
            filter = new HashMap();
        }
        if (projection == null) {
            projection = new HashMap();
        }
        if (sort == null) {
            sort = new HashMap();
        }
        Document dp = new Document(projection);
        Document df = new Document(filter);
        Document ds = new Document(sort);
        MongoCursor<Document> cursor = collection.find(df).projection(dp).sort(ds).iterator();
        while (cursor.hasNext()) {
            Document next = cursor.next();
            result.addToList(next);
        }
        result.setTotalRecordCount(collection.count(df));
        this.disconnect();
        return result;
    }

    @Override
    public CrudListResult getPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize) {
        this.connect();
        CrudListResult result = new CrudListResult();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        if (filter == null) {
            filter = new HashMap();
        }
        if (projection == null) {
            projection = new HashMap();
        }
        Document dp = new Document(projection);
        Document df = new Document(filter);
        MongoCursor<Document> cursor = collection.find(df).projection(dp).limit(pageSize).skip(page).iterator();
        while (cursor.hasNext()) {
            Document next = cursor.next();
            result.addToList(next);
        }
        result.setTotalRecordCount(collection.count(df));
        this.disconnect();
        return result;
    }

    @Override
    public CrudListResult getSortedPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize, Map<String, Object> sort) {
        this.connect();
        CrudListResult result = new CrudListResult();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        if (filter == null) {
            filter = new HashMap();
        }
        if (projection == null) {
            projection = new HashMap();
        }
        if (sort == null) {
            sort = new HashMap();
        }
        Document dp = new Document(projection);
        Document df = new Document(filter);
        Document ds = new Document(sort);
        MongoCursor<Document> cursor = collection.find(df).projection(dp).limit(pageSize).skip(page).sort(ds).iterator();
        while (cursor.hasNext()) {
            Document next = cursor.next();
            result.addToList(next);
        }
        result.setTotalRecordCount(collection.count(df));
        this.disconnect();
        return result;
    }

    @Override
    public Map<String, Object> create(String dbName, String tableName, Map<String, Object> rec) {
        if (isSlaveServer) {
            throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_SLAVE_SERVER);
        }
        this.connect();
        Map<String, Object> result = rec;
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        collection.insertOne(new Document(rec));
        this.disconnect();
        return result;
    }

    @Override
    public Map<String, Object> read(String dbName, String tableName, Map<String, Object> find) {
        if (isSlaveServer) {
            throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_SLAVE_SERVER);
        }
        this.connect();
        Map<String, Object> result = null;
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        Document df = new Document(find);
        MongoCursor<Document> cursor = collection.find(df).iterator();
        if (cursor.hasNext()) {
            result = BasicDBObject.parse(cursor.next().toJson());
        }
        this.disconnect();
        return result;
    }

    @Override
    public Map<String, Object> update(String dbName, String tableName, Map<String, Object> rec) {
        if (isSlaveServer) {
            throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_SLAVE_SERVER);
        }
        this.connect();
        Map<String, Object> result = rec;
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        Document dr = new Document(rec);
        Document df = new Document(_ID, dr.getObjectId(_ID));
        collection.updateOne(df, new Document("$set", dr));
        this.disconnect();
        return result;
    }

    @Override
    public Map<String, Object> delete(String dbName, String tableName, Map<String, Object> rec) {
        if (isSlaveServer) {
            throw new UnsupportedOperationException(NOT_SUPPORTED_FOR_SLAVE_SERVER);
        }
        this.connect();
        Map<String, Object> result = rec;
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        Document dr = new Document(rec);
        Document df = new Document(_ID, dr.getObjectId(_ID));
        collection.deleteOne(df);
        this.disconnect();
        return result;
    }

    public CrudListResult executeAggregation(String dbName, String tableName, List<Document> list) {
        CrudListResult result = new CrudListResult();
        this.connect();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(tableName);
        MongoCursor<Document> cursor = collection.aggregate(list).iterator();
        int i = 0;
        while (cursor.hasNext()) {
            Document next = cursor.next();
            result.addToList(next);
            i++;
        }
        result.setTotalRecordCount(i);
        this.disconnect();
        return result;
    }

    public CrudListResult executeListQuery(String dbName, String tableName, String query, int batchSize) {
        this.connect();
        CrudListResult result = new CrudListResult();
        MongoDatabase db = client.getDatabase(dbName);
        Document buildInfoResults = db.runCommand(Document.parse(query));
        Document cursor = (Document) buildInfoResults.get(CURSOR);
        Long cursorId = cursor.getLong(ID);
        List<Document> firstBatch = (List<Document>) cursor.get(FIRST_BATCH);
        result.addAll(firstBatch);

        if (cursorId != null && cursorId > 0) {
            Document nextResults = db.runCommand(new Document(MORE, cursorId)
                    .append(COLLECTION, tableName)
                    .append(BATCH_SIZE, batchSize)
            );
            List<Document> batch = (List<Document>) ((Document) nextResults.get(CURSOR)).get(NEXT_BATCH);
            result.addAll(batch);
            while (batch != null && !batch.isEmpty()) {
                nextResults = db.runCommand(new Document(MORE, cursorId).append(COLLECTION, tableName).append(BATCH_SIZE, batchSize));
                batch = (List<Document>) nextResults.get(NEXT_BATCH);
                if (batch != null) {
                    result.addAll(batch);
                }
            }
        }
        result.setTotalRecordCount(result.size());
        this.disconnect();
        return result;
    }

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

}
