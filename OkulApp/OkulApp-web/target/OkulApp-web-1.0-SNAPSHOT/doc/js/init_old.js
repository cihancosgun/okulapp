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


//############################ MENU ##############################################
okulappDb.menu.insert({name: "school", title: "Okul", titleEn: "School", icon: "fa fa-dashboard", link: "/pages/dashboards.xhtml", crudFormCode: null, order: NumberInt(1)});
okulappDb.menu.insert({name: "reports", title: "Raporlar", titleEn: "Reports", icon: "fa fa-file-text-o", link: "/pages/reports.xhtml", crudFormCode: null, order: NumberInt(2)});
okulappDb.menu.insert({name: "configs", title: "Ayarlar", titleEn: "Configuration", icon: "fa fa-cogs", link: "#", crudFormCode: null, order: NumberInt(3)});

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



//############################ MENU END ##############################################



//############################ SUB MENU ##############################################
okulappDb.subMenu.insert({name: "reportss", parentMenuName: "reports", title: "Rapor Oluştur", titleEn: "Create Report", icon: "fa fa-circle-o", link: "/pages/reports.xhtml", crudFormCode: null, order: NumberInt(0)});
okulappDb.subMenu.insert({name: "categories", parentMenuName: "reports", title: "Rapor Kategorileri", titleEn: "Report Categories", icon: "fa fa-circle-o", link: "#", crudFormCode: "categories", order: NumberInt(1)});
okulappDb.subMenu.insert({name: "reportQueries", parentMenuName: "reports", title: "Raporlar", titleEn: "Reports", icon: "fa fa-circle-o", link: "#", crudFormCode: "reports", order: NumberInt(2)});
okulappDb.subMenu.insert({name: "reportColumns", parentMenuName: "reports", title: "Rapor Alanları", titleEn: "Report Fields", icon: "fa fa-circle-o", link: "#", crudFormCode: "reportColumns", order: NumberInt(3)});
okulappDb.subMenu.insert({name: "connections", parentMenuName: "reports", title: "Veritabanı Bağlantıları", titleEn: "Connections", icon: "fa fa-circle-o", link: "#", crudFormCode: "connections", order: NumberInt(4)});
okulappDb.subMenu.insert({name: "filters", parentMenuName: "reports", title: "Filtreler", titleEn: "Filters", icon: "fa fa-circle-o", link: "#", crudFormCode: "filters", order: NumberInt(5)});



okulappDb.subMenu.insert({name: "forms", parentMenuName: "configs", title: "Formlar", titleEn: "Forms", icon: "fa fa-circle-o", link: "#", crudFormCode: "forms", order: NumberInt(1)});
okulappDb.subMenu.insert({name: "formFields", parentMenuName: "configs", title: "Form Alanları", titleEn: "Form Fields", icon: "fa fa-circle-o", link: "#", crudFormCode: "formFields", order: NumberInt(2)});
okulappDb.subMenu.insert({name: "menus", parentMenuName: "configs", title: "Menü", titleEn: "Menu", icon: "fa fa-circle-o", link: "#", crudFormCode: "menus", order: NumberInt(3)});
okulappDb.subMenu.insert({name: "subMenus", parentMenuName: "configs", title: "Alt Menüler", titleEn: "Sub Menu", icon: "fa fa-circle-o", link: "#", crudFormCode: "subMenus", order: NumberInt(4)});
okulappDb.subMenu.insert({name: "roles", parentMenuName: "configs", title: "Roller", titleEn: "Roles", icon: "fa fa-circle-o", link: "#", crudFormCode: "roles", order: NumberInt(5)});



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

//############################ SUB MENU END ##############################################


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





//############################ CONNECTIONS ##############################################

okulappDb.crudforms.insert({
    crudFormCode: "connections", tableName: "connections", title: "Veritabanı Bağlantıları", titleEn: "DB Connections", order: NumberInt(3)
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "connectionName", name: "connectionName", label: "Bağlantı Adı", labelEn: "Connection Name",
    required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "hostName", name: "hostName", label: "Sunucu Adı / IP Adresi", labelEn: "Host Name Or IP",
    required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(2), showList: true
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "port", name: "port", label: "Port No.", labelEn: "Port No.",
    required: true, controllClassName: "InputIntegerFormControl", defaultValue: "", order: NumberInt(3), showList: true
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "slave", name: "slave", label: "İkincil Sunucu", labelEn: "İkincil Sunucu",
    required: true, controllClassName: "InputCheckBoxControl", defaultValue: false, order: NumberInt(4), showList: true
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "dbName", name: "dbName", label: "Veritabanı", labelEn: "DB",
    required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: true
});

okulappDb.formFields.insert({crudFormCode: "connections", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(6)
});

//############################ CONNECTIONS END ##############################################

//############################ FILTERS ##############################################

okulappDb.crudforms.insert({
    crudFormCode: "filters", tableName: "filters", title: "Filtreler", titleEn: "Filters", order: NumberInt(3)
});

okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "filterCode", name: "filterCode", label: "Filtre Kodu", labelEn: "Filter Code", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true});

okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "filterName", name: "filterName", label: "Filtre Adı", labelEn: "Filter Name",
    required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true
});

okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "connection", name: "connection", label: "Bağlantı", labelEn: "Connection",
    required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null, order: NumberInt(2), showList: true,
    dbName: "okulapp", tableName: "connections", itemQuery: "", itemValueField: "_id", itemLabelField: "connectionName"
});

okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "controllClassName", name: "controllClassName", label: "Form Nesnesi", labelEn: "Form Controller", required: true, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["InputTextFormControl", "InputCheckBoxControl", "InputDateFormControl", "InputDoubleFormControl", "InputIntegerFormControl", "InputMaskFormControl", "InputSelectOneDbFormControl", "InputSelectOneFormControl", "InputSelectManyDbFormControl", "InputSelectManyFormControl", "InputCodeMirrorFormControl"], order: NumberInt(3)});

okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order", required: false, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(4), showList: true});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "dbName", name: "dbName", label: "Veritabanı", labelEn: "DB Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(5), showList: false});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "tableName", name: "tableName", label: "Tablo Adı", labelEn: "Table Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(6), showList: false});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "itemQuery", name: "itemQuery", label: "Liste Sorgu", labelEn: "Liste Sorgu", required: false, order: NumberInt(7), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "itemValueField", name: "itemValueField", label: "Değer Alanı", labelEn: "Item Value Field Name", required: false, order: NumberInt(8), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "itemLabelField", name: "itemLabelField", label: "Etiket Alanı", labelEn: "Item Label Name", order: NumberInt(9), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "filters", fieldName: "items", name: "items", label: "Liste Elemanları", labelEn: "Items", order: NumberInt(10), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});


//############################ FILTERS END ##############################################


//############################ Report Category #################################################

okulappDb.crudforms.insert({
    crudFormCode: "categories", tableName: "categories", title: "Kategoriler", titleEn: "Categories", order: NumberInt(3)
});

okulappDb.formFields.insert({crudFormCode: "categories", fieldName: "categoryName", name: "categoryName", label: "Kategori Adı", labelEn: "Category Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true});

okulappDb.formFields.insert({crudFormCode: "categories", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(5)
});


//############################ Report Category END #############################################


//############################ Report #################################################

var sampleQuery = "{\r\n    aggregate: \"dataBankEmployeesByDepartments\",\r\n    pipeline: [\r\n        {$lookup:\r\n                    {\r\n                        from: \"common\",\r\n                        localField: \"member\",\r\n                        foreignField: \"_id\",\r\n                        as: \"emp_dep_member\"\r\n                    }},\r\n        {\r\n            $lookup:\r\n                    {\r\n                        from: \"common\",\r\n                        localField: \"period\",\r\n                        foreignField: \"_id\",\r\n                        as: \"emp_dep_period\"\r\n                    }\r\n        },\r\n        {\r\n            $lookup:\r\n                    {\r\n                        from: \"dataBankSetting\",\r\n                        localField: \"department\",\r\n                        foreignField: \"_id\",\r\n                        as: \"emp_dep\"\r\n                    }\r\n        },\r\n        {\r\n            $lookup:\r\n                    {\r\n                        from: \"dataBankSetting\",\r\n                        localField: \"gender\",\r\n                        foreignField: \"_id\",\r\n                        as: \"emp_gender\"\r\n                    }\r\n        },\r\n        {\r\n            $project:\r\n                    {\r\n                        personelAmount: 1, \"emp_dep_member.name\": 1, \"emp_dep_period.name\": 1, \"emp_dep.name\": 1, \"emp_gender.name\": 1\r\n                    }\r\n        }\r\n    ],\r\n    cursor: {},\r\n    allowDiskUse: true\r\n}";

okulappDb.crudforms.insert({
    crudFormCode: "reports", tableName: "reports", title: "Raporlar", titleEn: "Reports", order: NumberInt(3)
});

okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "category", name: "category", label: "Kategori", labelEn: "Category", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null, order: NumberInt(0), showList: false,
    dbName: "okulapp", tableName: "categories", itemQuery: "", itemValueField: "_id", itemLabelField: "categoryName"
});
okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "reportName", name: "reportName", label: "Rapor Adı", labelEn: "Report Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true});
okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "connection", name: "connection", label: "Veritabanı", labelEn: "DB", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null, order: NumberInt(2), showList: false,
    dbName: "okulapp", tableName: "connections", itemQuery: "", itemValueField: "_id", itemLabelField: "connectionName"
});
okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "filters", name: "filters", label: "Filtre Alanları", labelEn: "Filters", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null, order: NumberInt(3), showList: false,
    dbName: "okulapp", tableName: "filters", itemQuery: "", itemValueField: "filterCode", itemLabelField: "filterName"
});
okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "reportQuery", name: "reportQuery", label: "Rapor Sorgusu", labelEn: "Report Query", required: true, controllClassName: "InputCodeMirrorFormControl", defaultValue: sampleQuery, order: NumberInt(4), showList: false, mode: "javascript"});

okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "roles", name: "roles", label: "Yetkili Roller", labelEn: "Authorized Roles", required: true, controllClassName: "InputSelectManyDbFormControl", defaultValue: null,
    dbName: "okulapp", tableName: "roles", itemQuery: "{enabled:true}", itemValueField: "roleByte", itemLabelField: "roleName", order: NumberInt(5)
});
okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "tableName", name: "tableName", label: "Tablo Adı", labelEn: "Table Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(6), showList: true});

okulappDb.formFields.insert({crudFormCode: "reports", fieldName: "reportQueryType", name: "reportQueryType", label: "Rapor Sorgu Tipi", labelEn: "Report Query Type", required: true, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["Aggregation", "JS Function"], order: NumberInt(7)});

//############################ Report END #############################################

//############################ Report Columns #################################################

okulappDb.crudforms.insert({
    crudFormCode: "reportColumns", tableName: "reportColumns", title: "Rapor Alanları", titleEn: "Report Fields", order: NumberInt(4)
});

okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "report", name: "report", label: "Rapor", labelEn: "Report", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null, order: NumberInt(0), showList: true,
    dbName: "okulapp", tableName: "reports", itemQuery: "", itemValueField: "_id", itemLabelField: "reportName"
});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "fieldName", name: "fieldName", label: "Alan Adı", labelEn: "Field Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(1), showList: true});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "label", name: "label", label: "Etiket", labelEn: "Label Name", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(2), showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "labelEn", name: "labelEn", label: "Etiket İngilizce", labelEn: "Label Name English", required: true, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(3), showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "order", name: "order", label: "Sıra No.", labelEn: "Order No.", required: true, controllClassName: "InputIntegerFormControl", defaultValue: null, order: NumberInt(5), showList: true});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "type", name: "type", label: "Veri Tipi", labelEn: "Type", required: true, controllClassName: "InputSelectOneDbFormControl", defaultValue: null, order: NumberInt(6), showList: true,
    dbName: "okulapp", tableName: "columnTypes", itemQuery: "", itemValueField: "code", itemLabelField: "name"
});

okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "controllClassName", name: "controllClassName", label: "Form Nesnesi", labelEn: "Form Controller", required: true, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["InputTextFormControl", "InputCheckBoxControl", "InputDateFormControl", "InputDoubleFormControl", "InputIntegerFormControl", "InputMaskFormControl", "InputSelectOneDbFormControl", "InputSelectOneFormControl", "InputSelectManyDbFormControl", "InputSelectManyFormControl", "InputCodeMirrorFormControl"], order: NumberInt(7)});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "dbName", name: "dbName", label: "Veritabanı", labelEn: "DB Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(8), showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "tableName", name: "tableName", label: "Tablo Adı", labelEn: "Table Name", required: false, controllClassName: "InputTextFormControl", defaultValue: "", order: NumberInt(9), showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "itemQuery", name: "itemQuery", label: "Liste Sorgu", labelEn: "Liste Sorgu", required: false, order: NumberInt(10), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "itemValueField", name: "itemValueField", label: "Değer Alanı", labelEn: "Item Value Field Name", required: false, order: NumberInt(11), controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "itemLabelField", name: "itemLabelField", label: "Etiket Alanı", labelEn: "Item Label Name", order: NumberInt(12), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "items", name: "items", label: "Liste Elemanları", labelEn: "Items", order: NumberInt(13), required: false, controllClassName: "InputTextFormControl", defaultValue: "", showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "pivotField", name: "pivotField", label: "Pivot Alanı", labelEn: "Pivot Field", required: false, controllClassName: "InputCheckBoxControl", defaultValue: false, order: NumberInt(14), showList: false});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "pivotArea", name: "pivotArea", label: "Pivot Bölgesi", labelEn: "Pivot Area", required: false, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["Row", "Column", "Measure"], order: NumberInt(15)});
okulappDb.formFields.insert({crudFormCode: "reportColumns", fieldName: "pivotMeasureType", name: "pivotMeasureType", label: "Pivot Ölçüm Tipi", labelEn: "Pivot Measure Type", required: false, controllClassName: "InputSelectOneFormControl", defaultValue: null,
    items: ["$sum", "$avg", "$count"], order: NumberInt(16)});

//############################ Report Columns END #############################################


//######################## DEMO DATA BEGIN #############################################


okulappDb.categories.drop();
okulappDb.categories.insert(
        {
            "_id": ObjectId("5bc881cbcf21b80c6802ef51"),
            "categoryName": "Faaliyet Raporları",
            "roles": [
                "0"
            ]
        });

okulappDb.reports.drop();

okulappDb.reports.insert(
        {
            "_id": ObjectId("5bcf0517cf21b867c5e1d793"),
            "accessType": "0",
            "reportQuery": "{\r\n   aggregate: \"dataBankSubsidiries\",\r\n   pipeline: [\r\n      {  $lookup:\r\n       {\r\n         from: \"common\",\r\n         localField: \"member\",\r\n         foreignField: \"_id\",\r\n         as: \"emp_dep_member\"\r\n       }},\r\n   {\r\n     $lookup:\r\n       {\r\n         from: \"common\",\r\n         localField: \"period\",\r\n         foreignField: \"_id\",\r\n         as: \"emp_dep_period\"\r\n       }\r\n  },\r\n{\r\n   $sort : {\"order\":1, \"emp_dep_member.name\":1, \"emp_dep_period.order\":1}\r\n} ,\r\n  {\r\n     $project:\r\n\t{\r\n\t    \"memberName\": { \"$arrayElemAt\": [\"$emp_dep_member.name\",0] }, \r\n\t    \"periodName\" : { \"$arrayElemAt\": [\"$emp_dep_period.name\",0] }, \r\n\t     capitalAmount:  { $divide: [ \"$capitalAmount\", 100 ] } , consolidated:1, order:1, paidCapital:{ $divide: [ \"$paidCapital\", 100 ] }, share: 1, subsidiriesTradeTitle:1\r\n\t}\r\n  }\r\n   ],\r\n   cursor: { },\r\n   allowDiskUse:true\r\n} ",
            "reportName": "İştirakler",
            "connection": ObjectId("5bc6e2cccf21b82a45fec416"),
            "filters": [
                "period",
                "member"
            ],
            "category": ObjectId("5bc881cbcf21b80c6802ef51"),
            "roles": [
                0
            ],
            "tableName": "dataBankSubsidiries",
            "columns": "memberName,periodName,subsidiriesTradeTitle,capitalAmount, consolidated,paidCapital, share",
            "columnTexts": "Kurum,Dönem,İştirakin Ticari Ünvanı, İştirak Tutarı (TL), Konsolidasyon,Ödenmiş Sermaye (TL), Pay(%)",
            "reportQueryType": "Aggregation"
        });


okulappDb.accessTypes.drop();
okulappDb.accessTypes.insert({"_id": ObjectId("5bc8943b76476706494e0be9"), "code": "0", "name": "Sadece Kendim"});
okulappDb.accessTypes.insert({"_id": ObjectId("5bc8943b76476706494e0bea"), "code": "1", "name": "Eşit Yetkiye Sahip Kullanıcılar"});
okulappDb.accessTypes.insert({"_id": ObjectId("5bc8943b76476706494e0beb"), "code": "2", "name": "Herkes"});



okulappDb.connections.drop();
okulappDb.connections.insert({"_id": ObjectId("5bc6e2cccf21b82a45fec416"), "hostName": "localhost", "port": NumberInt(27017), "dbName": "uysdb", "connectionName": "UYS", "roles": ["0"], "slave": false});
okulappDb.connections.insert({"_id": ObjectId("5bc6f3f4cf21b82a45fec48f"), "hostName": "localhost", "port": NumberInt(27017), "dbName": "okulapp", "roles": ["0"], "connectionName": "OKULAPP", "slave": false});


okulappDb.filters.drop();
okulappDb.filters.insert({"_id": ObjectId("5bc6f5d3cf21b82a45fec4bc"), "itemLabelField": "name", "filterCode": "memberType", "dbName": "okulapp", "filterName": "Kurum Tipi", "itemValueField": "code", "connection": ObjectId("5bc6e2cccf21b82a45fec416"), "controllClassName": "InputSelectManyDbFormControl", "items": "", "itemQuery": "", "order": 1, "tableName": "memberTypes"});
okulappDb.filters.insert({"_id": ObjectId("5bc89511cf21b80c6802efc2"), "itemLabelField": "name", "filterCode": "member", "dbName": "uysdb", "filterName": "Kurumlar", "itemValueField": "_id", "connection": ObjectId("5bc6e2cccf21b82a45fec416"), "controllClassName": "InputSelectManyDbFormControl", "items": "", "itemQuery": "{\"forms\":\"member\"}", "order": 2, "tableName": "common"});
okulappDb.filters.insert({"_id": ObjectId("5bd86488cf21b8630dd1cd32"), "itemLabelField": "name", "filterCode": "period", "dbName": "uysdb", "filterName": "Dönemler", "itemValueField": "_id", "connection": ObjectId("5bc6e2cccf21b82a45fec416"), "controllClassName": "InputSelectManyDbFormControl", "items": "", "itemQuery": "{\"forms\":\"period\"}", "order": 2, "tableName": "common"});


okulappDb.reportColumns.drop();
okulappDb.reportColumns.insert({"_id": ObjectId("5bd83520cf21b86ef4a1dfff"), "fieldName": "memberName", "labelEn": "Member", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "Kurum", "type": "text", "order": 1, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputTextFormControl", "items": "", "pivotField": true, "pivotArea": "Row", "pivotMeasureType": null});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd83533cf21b86ef4a1e004"), "fieldName": "periodName", "labelEn": "Period", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "Dönem", "type": "text", "order": 2, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputTextFormControl", "items": "", "pivotField": true, "pivotArea": "Column", "pivotMeasureType": null});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd83556cf21b86ef4a1e007"), "fieldName": "subsidiriesTradeTitle", "labelEn": "Subsidiries Trade Title", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "İştirakin Ticari Ünvanı", "type": "text", "order": 3, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputTextFormControl", "items": "", "pivotField": false, "pivotArea": "Column", "pivotMeasureType": null});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd8356ecf21b86ef4a1e00a"), "fieldName": "capitalAmount", "labelEn": "Capital Amount", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "İştirak Tutarı (TL)", "type": "money", "order": 4, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputDoubleFormControl", "items": "", "pivotField": true, "pivotArea": "Measure", "pivotMeasureType": "$sum"});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd835a0cf21b86ef4a1e00e"), "fieldName": "consolidated", "labelEn": "Consolidated", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "Konsolidasyon", "type": "text", "order": 5, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputTextFormControl", "items": "", "pivotField": false, "pivotArea": "Column", "pivotMeasureType": null});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd835b6cf21b86ef4a1e011"), "fieldName": "paidCapital", "labelEn": "Paid Capital", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "Ödenmiş Sermaye (TL)", "type": "money", "order": 6, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputDoubleFormControl", "items": "", "pivotField": true, "pivotArea": "Measure", "pivotMeasureType": "$sum"});
okulappDb.reportColumns.insert({"_id": ObjectId("5bd835cbcf21b86ef4a1e014"), "fieldName": "share", "labelEn": "Share(%)", "report": ObjectId("5bcf0517cf21b867c5e1d793"), "label": "Pay(%)", "type": "percent", "order": 7, "dbName": "", "itemQuery": "", "tableName": "", "itemLabelField": "", "itemValueField": "", "controllClassName": "InputDoubleFormControl", "items": "", "pivotField": true, "pivotArea": "Measure", "pivotMeasureType": "$sum"});

okulappDb.roles.drop();
okulappDb.roles.insert({"_id": ObjectId("5bbf3d7bcf21b86afd833d44"), "roleName": "uysarchitect", "enabled": true, "roleByte": "0"});

//######################## DEMO DATA END   #############################################



var filter = {
    "period": {
        "$in": [
            ObjectId("591a9d82139f0be7f1963257"),
            ObjectId("598d63e9139f0be7f1a35a04"),
            ObjectId("59f6e48b33093c51772d91c4"),
            ObjectId("5aa6235033093c51773f3b5d")
        ]
    },
    "memberType": {
        "$in": [
            "B,YB,KB,KTB,MB"
        ]
    }
};
function (filter, sort, sessionID, txID, tableName) {
    var uysdb = db.getSisterDB("uysdb");
    var db = db.getSisterDB("okulapp");
    print("Personel Sayısı Departman Sorgusu çalıştırılıyor...");
    printjson(filter);
    printjson(sort);
    if (filter.memberType != null && filter.member == null) {
        var memberType = [];
        for (var item in filter.memberType.$in) {
            memberType.push.apply(memberType, filter.memberType.$in[item].split(","));
        }
        var members = [];
        uysdb.common.find({memberType: {$in: memberType}}, {_id: 1}).forEach(function (x) {
            members.push(x._id);
        });
        delete filter.memberType;
        filter.member = {$in: members};
    }
    var retVal = {writedToTable: "OK"};
    uysdb.dataBankEmployeesByDepartments.find(filter).sort(sort).forEach(function (x) {
        var rec = {};
        rec.personelAmount = NumberInt(x.personelAmount);
        rec.member = db.common.findOne(x.member).name;
        rec.period = NumberInt(db.common.findOne(x.period).value);
        rec.department = db.dataBankSetting.findOne(x.department).name;
        rec.sessionID = sessionID;
        rec.txID = txID;
        db.getCollection(tableName).insert(rec);
    });
    print("Personel Sayısı Departman Sorgusu çalıştırıldı...");
    return retVal;
}