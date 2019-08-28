/*
 * 
 * 
 * 
 */
package com.okulapp.data;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class MyFileUpDownManager {

    private MongoDataAdapter ada;
    private String dbName;
    private String fsName;

    public MyFileUpDownManager() {
    }

    public MongoDataAdapter getAda() {
        return ada;
    }

    public void setAda(MongoDataAdapter ada) {
        this.ada = ada;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    private GridFSBucket getBucket() {
        MongoDatabase db = ada.getClient().getDatabase(dbName);
        GridFSBucket gridFSBucket = GridFSBuckets.create(db);
        return gridFSBucket;
    }

    public ObjectId uploadFile(InputStream fileInputStream, String fileName) {
        try {
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(358400);
            ada.connect();
            ObjectId fileId = getBucket().uploadFromStream(fileName, fileInputStream, options);
            ada.disconnect();
            return fileId;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "uploadFile Error", e);
            return null;
        }
    }

    public byte[] downloadFile(ObjectId fileId) {
        try {
            ByteArrayOutputStream streamToDownloadTo = new ByteArrayOutputStream();
            ada.connect();
            getBucket().downloadToStream(fileId, streamToDownloadTo);
            byte[] bytes = streamToDownloadTo.toByteArray();
            streamToDownloadTo.close();
            ada.disconnect();
            return bytes;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "uploadFile Error", e);
            return null;
        }

    }
}
