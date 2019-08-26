/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.data;

import com.okulapp.crud.dao.CrudDao;

/**
 *
 * @author cihan
 */
public interface DataAdapter extends CrudDao {

    public void connect();

    public void disconnect();

}
