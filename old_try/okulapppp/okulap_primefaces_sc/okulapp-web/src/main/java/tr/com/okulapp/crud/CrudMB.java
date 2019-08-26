/*
 * 
 * 
 * 
 */
package tr.com.okulapp.crud;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Named(value = "crudMB")
@SessionScoped
public class CrudMB implements Serializable {

    /**
     * Creates a new instance of CrudMB
     */
    public CrudMB() {
    }

    private String crudFormURL;

    public String getCrudFormURL() {
        return crudFormURL;
    }

    public void setCrudFormURL(String crudFormURL) {
        this.crudFormURL = crudFormURL;
    }

}
