Proje Oluşturma Tarihi : 16.08.2019
Yazan : Cihan Coşgun
Proje Amacı: Okul
Mongo Java Driver : 3.4.2 kullanılmıştır. (Eval fonksiyonu kullanılmıştır!!);

=== Realm Config
https://github.com/cihancosgun/mongo-realm
download mongo-realm jar and put it in your glassfish domains lib folder (i.e. $GLASSFISH_HOME/glassfish/domains/$DOMAINNAME/lib/)
at the end of $GLASSFISH_HOME/glassfish/domains/$DOMAINNAME/config/login.conf file paste:
mongoRealm { 
  com.tadamski.glassfish.mongo.realm.MongoLoginModule required; 
};

asadmin delete-auth-realm okulAppRealm;
asadmin create-auth-realm --classname com.tadamski.glassfish.mongo.realm.MongoRealm --property jaas-context=mongoRealm:mongo.db.name=okulapp okulAppRealm;

Custom configuration:

Of course defaults can be overriden. Simply add properties to realm created in 3rd step of Setup.

Property name	Default value
mongo.hostname	localhost
mongo.port	27017
mongo.db.name	okulapp
mongo.collection.name	users
login.property	login
salt.property	salt
password.property	password
groups.property	groups
hash.function	SHA-512


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


asadmin delete-jms-resource jms/bilgiyuvamQueue;
asadmin create-jms-resource \
    --user admin --restype javax.jms.Queue \
    --description "Bilgi Yuvam İşlem Kuyruğu" \
    --property Name=bilgiYuvamQueue \
    jms/bilgiyuvamQueue;

# Connection Factories
asadmin delete-jms-resource jms/bilgiyuvamQueueConnectionFactory
asadmin create-jms-resource --restype javax.jms.QueueConnectionFactory --description "Bilgiyuvam Queue Connection Factory" jms/bilgiyuvamQueueConnectionFactory




############################################ ip tables 80 to 8080 redirect ####################################


#4848,2707 portu kapat
iptables -A INPUT -i ens192 -p tcp --dport 4848 -j DROP
iptables -A INPUT -i ens192 -p tcp --dport 27017 -j DROP


iptables -A INPUT -i ens192 -p tcp --dport 80 -j ACCEPT
iptables -A INPUT -i ens192 -p tcp --dport 8080 -j ACCEPT
iptables -A PREROUTING -t nat -i ens192 -p tcp --dport 80 -j REDIRECT --to-port 8080


iptables -A INPUT -i ens192 -p tcp --dport 443 -j ACCEPT
iptables -A INPUT -i ens192 -p tcp --dport 9443 -j ACCEPT
iptables -A PREROUTING -t nat -i ens192 -p tcp --dport 443 -j REDIRECT --to-port 9443

iptables -t nat -L

iptables -t nat -F #tümünü temizle




cd /home/cihan/github_cihan/okulapp/okulapp/OkulApp/OkulApp-ear/target

