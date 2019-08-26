/*
 
 mongo okulapp ~/git/OkulApp/OkulApp-web/src/main/webapp/doc/js/init.js
 
 */
"use strict";

var okulappDb = db.getSisterDB("okulapp");
okulappDb.menu.drop();
okulappDb.formFields.drop();
okulappDb.subMenu.drop();
okulappDb.crudforms.drop();


okulappDb.columnTypes.drop();
okulappDb.columnTypes.insert({code: "text", name: "Metin", nameEn: "Text"});
okulappDb.columnTypes.insert({code: "phone", name: "Telefon / Faks", nameEn: "Phone"});
okulappDb.columnTypes.insert({code: "email", name: "E-Posta", nameEn: "E-Mail"});
okulappDb.columnTypes.insert({code: "date", name: "Tarih", nameEn: "Date"});
okulappDb.columnTypes.insert({code: "hour", name: "Saat", nameEn: "Time"});
okulappDb.columnTypes.insert({code: "int", name: "Tam Sayı", nameEn: "Integer"});
okulappDb.columnTypes.insert({code: "double", name: "Küsürlü Sayı", nameEn: "Double"});
okulappDb.columnTypes.insert({code: "percent", name: "Yüzde", nameEn: "Percent"});
okulappDb.columnTypes.insert({code: "money", name: "Para Birimi", nameEn: "Money"});

// HELPER RECORDS END

// CRUD FORMS


// MENUS
okulappDb.crudforms.insert({
    crudFormCode: "menus", tableName: "menu", title: "Menü", titleEn: "Menu", order: NumberInt(1)
});


okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "name", name: "name", label: "Menü Adı", labelEn: "Menu Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(0), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "enabled", name: "enabled", label: "Aktif", labelEn: "Active", required: true, controllClassName: "InputCheckBoxControl", defaultValue: true, order: NumberInt(3), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(4)
});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "title", name: "title", label: "Başlık", labelEn: "Title", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "titleEn", name: "titleEn", label: "Başlık İngilizce", labelEn: "Title English", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(6), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order No.", required: true, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(7), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "icon", name: "icon", label: "Sembol", labelEn: "Icon", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(7), showList: true});
okulappDb.formFields.insert({crudFormCode: "menus", fieldName: "link", name: "link", label: "Link", labelEn: "Link", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(8), showList: true});
// MENUS END

//SUB MENUS


okulappDb.crudforms.insert({
    crudFormCode: "subMenus", tableName: "subMenu", title: "Alt Menüler", titleEn: "Sub Menus", order: NumberInt(2)
});


okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "parentMenuName", name: "parentMenuName", label: "Üst Menü", labelEn: "Parent Menu", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "menu", itemQuery: "", itemValueField: "name", itemLabelField: "name", order: NumberInt(0)
});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "name", name: "name", label: "Menü Adı", labelEn: "Menu Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(0), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "crudFormCode", name: "crudFormCode", label: "Form Kodu", labelEn: "Form Code", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: "", order: NumberInt(1), showList: true,
    dbName: "okulapp", tableName: "crudforms", itemQuery: "", itemValueField: "crudFormCode", itemLabelField: "crudFormCode"
});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "enabled", name: "enabled", label: "Aktif", labelEn: "Active", required: true, controllClassName: "InputCheckBoxControl", defaultValue: true, order: NumberInt(3), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(4)
});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "title", name: "title", label: "Başlık", labelEn: "Title", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "titleEn", name: "titleEn", label: "Başlık İngilizce", labelEn: "Title English", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(6), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order No.", required: true, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(7), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "icon", name: "icon", label: "Sembol", labelEn: "Icon", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(7), showList: true});
okulappDb.formFields.insert({crudFormCode: "subMenus", fieldName: "link", name: "link", label: "Link", labelEn: "Link", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(8), showList: true});


//SUB MENUS END


//############################ ROLES ##############################################
okulappDb.crudforms.insert({
    crudFormCode: "roles", tableName: "roles", title: "Roller", titleEn: "Roles", order: NumberInt(1)
});


okulappDb.formFields.insert({crudFormCode: "roles", fieldName: "roleName", name: "roleName", label: "Rol Adı", labelEn: "Role Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(2), showList: true});
okulappDb.formFields.insert({crudFormCode: "roles", fieldName: "enabled", name: "enabled", label: "Aktif", labelEn: "Active", required: true, controllClassName: "InputCheckBoxControl", defaultValue: true, order: NumberInt(3), showList: true});
okulappDb.formFields.insert({crudFormCode: "roles", fieldName: "roleByte", name: "roleByte", label: "No.", labelEn: "No.", required: true, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(1), showList: true});

//############################ ROLES END ##############################################


//############################ FORMS ##############################################

okulappDb.crudforms.insert({
    crudFormCode: "forms", tableName: "crudforms", title: "Formlar", titleEn: "Forms", order: NumberInt(2)
});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "crudFormCode", name: "crudFormCode", label: "Form Kodu", labelEn: "Form Code", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "tableName", name: "tableName", label: "Tablo Adı", labelEn: "Table Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(2), showList: true});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "enabled", name: "enabled", label: "Aktif", labelEn: "Active", required: true, controllClassName: "InputCheckBoxControl", defaultValue: true, order: NumberInt(3), showList: true});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(4)
});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "title", name: "title", label: "Başlık", labelEn: "Title", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: true});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "titleEn", name: "titleEn", label: "Başlık İngilizce", labelEn: "Title English", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(6), showList: true});
okulappDb.formFields.insert({crudFormCode: "forms", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order No.", required: true, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(7), showList: true});

//############################ FORMS END ##############################################


//############################ FORM FIELDS ##############################################

okulappDb.crudforms.insert({
    crudFormCode: "formFields", tableName: "formFields", title: "Form Alanları", titleEn: "Form Fields", order: NumberInt(3)
});

okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "crudFormCode", name: "crudFormCode",
    label: "Form Kodu", labelEn: "Form Code", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: "", order: NumberInt(1), showList: true,
    dbName: "okulapp", itemQuery: "", itemValueField: "crudFormCode", itemLabelField: "crudFormCode", tableName: "crudforms"
});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "fieldName", name: "fieldName", label: "Alan Adı", labelEn: "Field Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(2), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "name", name: "name", label: "Adı", labelEn: "Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(3), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "label", name: "label", label: "Etiket", labelEn: "Label", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(4), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "labelEn", name: "labelEn", label: "Etiket İngilizce", labelEn: "Label English", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "required", name: "required", label: "Zorunlu", labelEn: "Required", required: true, controllClassName: "InputCheckBoxControl", defaultValue: false, order: NumberInt(6), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "controllClassName", name: "controllClassName", label: "Form Nesnesi", labelEn: "Form Controller", required: true, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["InputTextFormControl", "InputCheckBoxControl", "InputDateFormControl", "InputDoubleFormControl", "InputIntegerFormControl", "InputMaskFormControl", "InputSelectOneDbFormControl", "InputSelectOneFormControl", "InputSelectManyDbFormControl", "InputSelectManyFormControl", "InputCodeMirrorFormControl"], order: NumberInt(7)});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "defaultValue", name: "defaultValue", label: "Varsayılan Değer", labelEn: "Default Value", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(8)});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "enabled", name: "enabled", label: "Aktif", labelEn: "Active", required: true, controllClassName: "InputCheckBoxControl", defaultValue: null, order: NumberInt(9)});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(10), tableName: "roles"
});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "showList", name: "showList", label: "Listede Göster", labelEn: "Show In List", required: true, controllClassName: "InputCheckBoxControl", defaultValue: false, order: NumberInt(10), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order", required: false, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(11), showList: true});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "dbName", name: "dbName", label: "Veritabanı", labelEn: "DB Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(12), showList: false});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "tableName", name: "tableName", label: "Tablo Adı", labelEn: "Table Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(12), showList: false});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "itemQuery", name: "itemQuery", label: "Liste Sorgu", labelEn: "Liste Sorgu", required: false, order: NumberInt(13), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "itemValueField", name: "itemValueField", label: "Değer Alanı", labelEn: "Item Value Field Name", required: false, order: NumberInt(14), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "itemLabelField", name: "itemLabelField", label: "Etiket Alanı", labelEn: "Item Label Name", order: NumberInt(15), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "formFields", fieldName: "items", name: "items", label: "Liste Elemanları", labelEn: "Items", order: NumberInt(16), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});

//############################ FORM FIELDS  END ##############################################




//CRUD FORMS END


//############################ MENU ##############################################
okulappDb.menu.insert({name: "school", title: "Okul", titleEn: "School", icon: "fa fa-dashboard", link: "/pages/dashboards.xhtml", crudFormCode: null, order: NumberInt(1)});
okulappDb.menu.insert({name: "configs", title: "Ayarlar", titleEn: "Configuration", icon: "fa fa-cogs", link: "#", crudFormCode: null, order: NumberInt(3)});



//############################ MENU END ##############################################



//############################ SUB MENU ##############################################
okulappDb.subMenu.insert({name: "branch", parentMenuName: "school", title: "Şube", titleEn: "Branch", icon: "fa fa-sitemap", link: "#", crudFormCode: "branch", order: NumberInt(0)});

okulappDb.subMenu.insert({name: "forms", parentMenuName: "configs", title: "Formlar", titleEn: "Forms", icon: "fa fa-circle-o", link: "#", crudFormCode: "forms", order: NumberInt(1)});
okulappDb.subMenu.insert({name: "formFields", parentMenuName: "configs", title: "Form Alanları", titleEn: "Form Fields", icon: "fa fa-circle-o", link: "#", crudFormCode: "formFields", order: NumberInt(2)});
okulappDb.subMenu.insert({name: "menus", parentMenuName: "configs", title: "Menü", titleEn: "Menu", icon: "fa fa-circle-o", link: "#", crudFormCode: "menus", order: NumberInt(3)});
okulappDb.subMenu.insert({name: "subMenus", parentMenuName: "configs", title: "Alt Menüler", titleEn: "Sub Menu", icon: "fa fa-circle-o", link: "#", crudFormCode: "subMenus", order: NumberInt(4)});
okulappDb.subMenu.insert({name: "roles", parentMenuName: "configs", title: "Roller", titleEn: "Roles", icon: "fa fa-circle-o", link: "#", crudFormCode: "roles", order: NumberInt(5)});



//############################ SUB MENU END ##############################################



///#################

/*
{
	"_id" : ObjectId("5d567168003ac45f92ac50ad"),
	"crudFormCode" : "menus",
	"fieldName" : "roles",
	"name" : "roles",
	"label" : "Yetkili Roller",
	"labelEn" : "Authorized Roles",
	"required" : true,
	"controllClassName" : "InputSelectManyDbFormControl",
	"defaultValue" : null,
	"dbName" : "okulapp",
	"tableName" : "roles",
	"itemQuery" : "{enabled:true}",
	"itemValueField" : "roleByte",
	"itemLabelField" : "roleName",
	"order" : 4
}
{
	"_id" : ObjectId("5d567168003ac45f92ac50b8"),
	"crudFormCode" : "subMenus",
	"fieldName" : "roles",
	"name" : "roles",
	"label" : "Yetkili Roller",
	"labelEn" : "Authorized Roles",
	"required" : true,
	"controllClassName" : "InputSelectManyDbFormControl",
	"defaultValue" : null,
	"dbName" : "okulapp",
	"tableName" : "roles",
	"itemQuery" : "{enabled:true}",
	"itemValueField" : "roleByte",
	"itemLabelField" : "roleName",
	"order" : 4
}
{
	"_id" : ObjectId("5d567168003ac45f92ac50c6"),
	"crudFormCode" : "forms",
	"fieldName" : "roles",
	"name" : "roles",
	"label" : "Yetkili Roller",
	"labelEn" : "Authorized Roles",
	"required" : true,
	"controllClassName" : "InputSelectManyDbFormControl",
	"defaultValue" : null,
	"dbName" : "okulapp",
	"tableName" : "roles",
	"itemQuery" : "{enabled:true}",
	"itemValueField" : "roleByte",
	"itemLabelField" : "roleName",
	"order" : 4
}
{
	"_id" : ObjectId("5d567168003ac45f92ac50d4"),
	"crudFormCode" : "formFields",
	"fieldName" : "roles",
	"name" : "roles",
	"label" : "Yetkili Roller",
	"labelEn" : "Authorized Roles",
	"required" : true,
	"controllClassName" : "InputSelectManyDbFormControl",
	"defaultValue" : null,
	"dbName" : "okulapp",
	"itemQuery" : "{enabled:true}",
	"itemValueField" : "roleByte",
	"itemLabelField" : "roleName",
	"order" : 10,
	"tableName" : "roles"
}
{
	"_id" : ObjectId("5d5d3a1e0f43db2ef15a55cb"),
	"fieldName" : "periods",
	"labelEn" : "Periods",
	"defaultValue" : "",
	"roles" : [
		0
	],
	"dbName" : "okulapp",
	"crudFormCode" : "classes",
	"label" : "Dönemler",
	"required" : true,
	"enabled" : true,
	"itemQuery" : "{}",
	"tableName" : "period",
	"showList" : false,
	"itemLabelField" : "periodName",
	"name" : "periods",
	"itemValueField" : "code",
	"ajaxUpdateFieldName" : "",
	"controllClassName" : "InputSelectManyDbFormControl",
	"items" : "",
	"order" : 1,
	"ajaxUpdateFieldQueryFieldName" : ""
}
 */