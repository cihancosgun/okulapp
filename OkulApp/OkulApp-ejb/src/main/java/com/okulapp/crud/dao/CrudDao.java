/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.crud.dao;

import java.util.Map;

/**
 *
 * @author cihan
 */
public interface CrudDao {

    public CrudListResult getList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection);

    public CrudListResult getSortedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, Map<String, Object> sort);

    public CrudListResult getPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize);

    public CrudListResult getSortedPagedList(String dbName, String tableName, Map<String, Object> filter, Map<String, Object> projection, int page, int pageSize, Map<String, Object> sort);

    public Map<String, Object> create(String dbName, String tableName, Map<String, Object> rec);

    public Map<String, Object> read(String dbName, String tableName, Map<String, Object> find);

    public Map<String, Object> update(String dbName, String tableName, Map<String, Object> rec);

    public Map<String, Object> delete(String dbName, String tableName, Map<String, Object> rec);
}
