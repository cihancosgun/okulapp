/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okulapp.data.okul;

import javax.ejb.Local;
import com.okulapp.data.AdvancedDataAdapter;

/**
 *
 * @author cihan
 */
@Local
public interface MyDataSBLocal {

    public AdvancedDataAdapter getAdvancedDataAdapter();

    public String getDbName();
}
