#!/bin/bash

KAFKA_CLIENT_PATH="./certs/kafka-client"

while getopts "c:k:" option; do
  case $option in
    c ) CA_CERT=$OPTARG
    ;;
    k ) CA_KEY=$OPTARG
    ;;
  esac
done

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
openssl x509 -req -CA $CA_CERT -CAkey $CA_KEY -in $KAFKA_CLIENT_PATH/kafka.client.csr -out $KAFKA_CLIENT_PATH/kafka.client.crt -days $VALIDITY -CAcreateserial 

# Add CA to keystore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_CERT -storepass $STOREPASS -keypass $KEYPASS


# Add signed cert to keystore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.keystore.jks -storetype $STORETYPE -alias $ALIAS -import -file $KAFKA_CLIENT_PATH/kafka.client.crt \
    -storepass $STOREPASS -keypass $KEYPASS

# Add CA to truststore
keytool -keystore $KAFKA_CLIENT_PATH/kafka.client.truststore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_CERT -storepass $STOREPASS -keypass $KEYPASS

rm $KAFKA_CLIENT_PATH/kafka.client.csr 
rm $KAFKA_CLIENT_PATH/kafka.client.crt