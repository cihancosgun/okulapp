Proje Oluşturma Tarihi : 16.08.2019
Yazan : Cihan Coşgun
Proje Amacı: Okul
Mongo Java Driver : 3.4.2 kullanılmıştır. (Eval fonksiyonu kullanılmıştır!!);

=== Realm Config

asadmin delete-auth-realm OpenDS
asadmin create-auth-realm \
--user admin \
--classname com.sun.enterprise.security.auth.realm.ldap.LDAPRealm \
--property \
jaas-context=ldapRealm:\
directory="ldap\://localhost\:1389":\
base-dn="dc\=example,dc\=com":\
assign-groups="ou\=Roles,dc\=example,dc\=com" \
OpenDS


=== Properties
asadmin delete-custom-resource okulapp_properties
asadmin create-custom-resource \
--user admin \
--restype java.util.Properties \
--factoryclass org.glassfish.resources.custom.factory.PropertiesFactory \
--property \
DB_TYPE="MONGO":\
DB_HOST="localhost":\
DB_PORT="27017":\
DB_NAME="okulapp" \
okulapp_properties;