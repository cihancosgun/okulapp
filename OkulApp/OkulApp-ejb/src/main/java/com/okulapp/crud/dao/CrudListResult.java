/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.crud.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author cihan
 */
public class CrudListResult extends ArrayList<Map<String, Object>> {

    public CrudListResult() {

    }

    private long totalRecordCount;

    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public void addToList(Map<String, Object> rec) {
        if (!this.contains(rec)) {
            this.add(rec);
        }
    }

    public void removeFromList(Map<String, Object> rec) {
        if (this.contains(rec)) {
            this.remove(rec);
        }
    }

}
