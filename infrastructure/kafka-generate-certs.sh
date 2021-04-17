#!/bin/bash

CERT_PATH="certs"
KAFKA_PATH="certs/kafka"

rm $KAFKA_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="kafka"
SAN="kafka"
CN="kafka"
VALIDITY="365"
KEYALG="RSA"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
    CN_ITERATION=$CN$i
    SAN_ITERATION=$SAN$i
    # Create keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION_UNIT, CN=$CN_ITERATION" -ext SAN=DNS:$SAN_ITERATION \
        -noprompt \
        -storepass confluent -keypass confluent
        

    # Generate csr
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -certreq -file $KAFKA_PATH/kafka$i.crt -storepass confluent -keypass confluent

    # Sign crt
    openssl x509 -req -CA $CERT_PATH/ca.crt -CAkey $CERT_PATH/ca.key -in $KAFKA_PATH/kafka$i.crt -out $KAFKA_PATH/kafka$i.signed.crt -days $VALIDITY -CAcreateserial  

    # Add CA to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias CAroot -import -file $CERT_PATH/ca.crt -storepass confluent -keypass confluent

    # Add signed cert to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -import -file $KAFKA_PATH/kafka$i.signed.crt -storepass confluent -keypass confluent

    # Add CA to truststore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.truststore.jks -alias CAroot -importcert -file $CERT_PATH/ca.crt -storepass confluent -keypass confluent

    echo "confluent" > $KAFKA_PATH/${i}_sslkey_creds        
    echo "confluent" > $KAFKA_PATH/${i}_keystore_creds
    echo "confluent" > $KAFKA_PATH/${i}_truststore_creds
done
