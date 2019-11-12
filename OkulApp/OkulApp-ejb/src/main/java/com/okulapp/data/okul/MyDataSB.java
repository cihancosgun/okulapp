/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.data.okul;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.okulapp.data.AdvancedDataAdapter;
import com.okulapp.data.MongoDataAdapter;
import com.okulapp.data.MyFileUpDownManager;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

/**
 *
 * @author cihan
 */
@Singleton
public class MyDataSB implements MyDataSBLocal {

    private AdvancedDataAdapter ada;
    private AdvancedDataAdapter.DbType dbType;
    private String dbName;
    private String dbHost;
    private Integer dbPort;
    private MyFileUpDownManager fileUpDownManager;

    Properties myProperties;

    public MyDataSB() {
        try {
            myProperties = InitialContext.doLookup("okulapp_properties");
        } catch (NamingException ex) {
            Logger.getLogger(MyDataSB.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.dbType = AdvancedDataAdapter.DbType.valueOf(myProperties.get("DB_TYPE").toString());
        this.dbName = myProperties.get("DB_NAME").toString();
        this.ada = new AdvancedDataAdapter(this.dbType);

        if (AdvancedDataAdapter.DbType.MONGO.equals(this.dbType)) {
            this.dbHost = myProperties.get("DB_HOST").toString();
            this.dbPort = Integer.parseInt(myProperties.get("DB_PORT").toString());
            MongoDataAdapter mda = (MongoDataAdapter) ada.getSelectedDataAdapter();
            mda.setMongoHost(dbHost);
            mda.setMongoPort(dbPort);
            fileUpDownManager = new MyFileUpDownManager();
            fileUpDownManager.setAda(mda);
            fileUpDownManager.setDbName(dbName);
            fileUpDownManager.setFsName("okulapp_files");
        }
    }

    @Override
    public AdvancedDataAdapter getAdvancedDataAdapter() {
        return ada;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public MyFileUpDownManager getFileUpDownManager() {
        return fileUpDownManager;
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(MyDataSB.class.getName()).info("MyDataSB init");
    }

    @PreDestroy
    public void destroy() {
        Logger.getLogger(MyDataSB.class.getName()).info("MyDataSB destroy");
    }

}
