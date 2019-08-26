/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import com.okulapp.crud.dao.CrudListResult;
import org.bson.Document;

/**
 *
 * @author cihan
 */
public class AdvancedDataAdapter implements DataAdapter {

    public enum DbType {
        MONGO, MYSQL, RAM, FILE
    }
    private DbType dbType;
    private DataAdapter selectedDataAdapter;

    public AdvancedDataAdapter() {
        dbType = DbType.MONGO;//Default db type is mongodb
        selectedDataAdapter = new MongoDataAdapter();
    }

    public AdvancedDataAdapter(DbType dbType) {
        this.dbType = dbType;
        switch (dbType) {
            case MONGO:
                selectedDataAdapter = new MongoDataAdapter();
                break;
        }
    }

    @Override
    public void connect() {
        selectedDataAdapter.connect();
    }

    @Override
    public void disconnect() {
        selectedDataAdapter.disconnect();
    }

    @Override
    public CrudListResult getList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection) {
        return selectedDataAdapter.getList(dbName, tableName, filter, projection);
    }

    @Override
    public CrudListResult getSortedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, Map<String, Object> sort) {
        return selectedDataAdapter.getSortedList(dbName, tableName, filter, projection, sort);
    }

    @Override
    public CrudListResult getPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize) {
        return selectedDataAdapter.getPagedList(dbName, tableName, filter, projection, page, pageSize);
    }

    @Override
    public CrudListResult getSortedPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize, Map<String, Object> sort) {
        return selectedDataAdapter.getSortedPagedList(dbName, tableName, filter, projection, page, pageSize, sort);
    }

    @Override
    public Map<String, Object> create(String dbName, String tableName, Map<String, Object> rec) {
        return selectedDataAdapter.create(dbName, tableName, rec);
    }

    @Override
    public Map<String, Object> read(String dbName, String tableName, Map<String, Object> find) {
        return selectedDataAdapter.read(dbName, tableName, find);
    }

    @Override
    public Map<String, Object> update(String dbName, String tableName, Map<String, Object> rec) {
        return selectedDataAdapter.update(dbName, tableName, rec);
    }

    @Override
    public Map<String, Object> delete(String dbName, String tableName, Map<String, Object> rec) {
        return selectedDataAdapter.delete(dbName, tableName, rec);
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public DataAdapter getSelectedDataAdapter() {
        return selectedDataAdapter;
    }

    public void setSelectedDataAdapter(DataAdapter selectedDataAdapter) {
        this.selectedDataAdapter = selectedDataAdapter;
    }

    public Map<String, Object> getMapFromString(String str) {
        Map<String, Object> result = new HashMap();
        if (str == null || str.isEmpty()) {
            return result;
        }
//        ObjectMapper mapper = new ObjectMapper();
        this.dbType = dbType;
        switch (dbType) {
            case MONGO: {
                result = Document.parse(str);
            }
            break;
        }

        return result;
    }
}
