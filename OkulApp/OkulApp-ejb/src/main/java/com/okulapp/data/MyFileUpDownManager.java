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
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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

    public BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
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
