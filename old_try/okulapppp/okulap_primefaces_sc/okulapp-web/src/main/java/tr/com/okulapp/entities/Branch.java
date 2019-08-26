/*
 * 
 * 
 * 
 */
package tr.com.okulapp.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Entity
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public String getAddress() {
        return address;
    }

    public City getCity() {
        return city;
    }

    public String getFax() {
        return fax;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Town getTown() {
        return town;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getWeb() {
        return web;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public String getTaxOfficeNo() {
        return taxOfficeNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branch)) {
            return false;
        }
        Branch other = (Branch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public void setTaxOfficeNo(String taxOfficeNo) {
        this.taxOfficeNo = taxOfficeNo;
    }

    @Override
    public String toString() {
        return "tr.com.okulapp.entities.Branch[ id=" + id + " ]";
    }

    private String name;
    private String address;

    @OneToOne(targetEntity = City.class)
    private City city;

    @OneToOne(targetEntity = Town.class)
    private Town town;

    private String postCode;
    private String phone;
    private String fax;
    private String email;
    private String web;
    private String taxOffice;
    private String taxOfficeNo;

}
