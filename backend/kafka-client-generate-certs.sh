#!/bin/bash

CA_PATH="./certs/ca"
KAFKA_CLIENT_PATH="./certs/kafka-client"

if [ ! -d $KAFKA_CLIENT_PATH ] 
then
    mkdir $KAFKA_CLIENT_PATH
fi

rm $KAFKA_CLIENT_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="kafka-client"

CN="kafka-client"

VALIDITY="365"
KEYALG="RSA"

STOREPASS="confluent"
KEYPASS="confluent"

STORETYPE="JKS"

# Create keystore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias $ALIAS -validity $VALIDITY -genkey -keyalg $KEYALG \
    -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION, CN=$CN" \
    -storepass $STOREPASS -keypass $KEYPASS

# Generate csr
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias $ALIAS -certreq -file $KAFKA_CLIENT_PATH/kafka.client.csr \
    -storepass $STOREPASS -keypass $KEYPASS

# Sign crt
openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $KAFKA_CLIENT_PATH/kafka.client.csr -out $KAFKA_CLIENT_PATH/kafka.client.crt -days $VALIDITY -CAcreateserial 

# Add CA to keystore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS -keypass $KEYPASS


# Add signed cert to keystore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias $ALIAS -import -file $KAFKA_CLIENT_PATH/kafka.client.crt \
    -storepass $STOREPASS -keypass $KEYPASS

# Add CA to truststore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.truststore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS -keypass $KEYPASS

rm $KAFKA_CLIENT_PATH/kafka.client.csr 
rm $KAFKA_CLIENT_PATH/kafka.client.crt

KAFKA_SERVER=$SERVER_FQDN:9092,$SERVER_FQDN:9093,$SERVER_FQDN:9094

echo "bootstrap.servers=$KAFKA_SERVER" > $KAFKA_CLIENT_PATH/client-ssl.properties
echo "security.protocol=SSL" >> $KAFKA_CLIENT_PATH/client-ssl.properties

echo ssl.truststore.location=$(pwd)/certs/kafka-client/kafka.client.truststore.jks >> $KAFKA_CLIENT_PATH/client-ssl.properties
echo ssl.keystore.location=$(pwd)/certs/kafka-client/kafka.client.keystore.jks >> $KAFKA_CLIENT_PATH/client-ssl.properties

echo "ssl.truststore.password=$STOREPASS" >> $KAFKA_CLIENT_PATH/client-ssl.properties
echo "ssl.keystore.password=$STOREPASS" >> $KAFKA_CLIENT_PATH/client-ssl.properties
echo "ssl.key.password=$KEYPASS" >> $KAFKA_CLIENT_PATH/client-ssl.properties