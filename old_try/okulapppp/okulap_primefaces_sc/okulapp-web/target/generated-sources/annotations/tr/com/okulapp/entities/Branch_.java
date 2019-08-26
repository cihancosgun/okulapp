package tr.com.okulapp.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Branch.class)
public abstract class Branch_ {

	public static volatile SingularAttribute<Branch, String> address;
	public static volatile SingularAttribute<Branch, Town> town;
	public static volatile SingularAttribute<Branch, City> city;
	public static volatile SingularAttribute<Branch, String> phone;
	public static volatile SingularAttribute<Branch, String> web;
	public static volatile SingularAttribute<Branch, String> name;
	public static volatile SingularAttribute<Branch, String> postCode;
	public static volatile SingularAttribute<Branch, Long> id;
	public static volatile SingularAttribute<Branch, String> taxOffice;
	public static volatile SingularAttribute<Branch, String> fax;
	public static volatile SingularAttribute<Branch, String> taxOfficeNo;
	public static volatile SingularAttribute<Branch, String> email;

}

