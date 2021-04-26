#!/bin/bash

CA_PATH="./certs"
KAFKA_PATH="./certs/kafka"

rm $KAFKA_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="kafka"

CN="kafka"
SAN="kafka"

VALIDITY="365"
KEYALG="RSA"

STOREPASS="confluent"
KEYPASS="confluent"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
    CN_ITERATION=$CN$i

    SAN_ITERATION="DNS:"$SAN$i

    STOREPASS_ITERATION="confluent"${i}
    KEYPASS_ITERATION="confluent"${i}

    # Create keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION, CN=$CN_ITERATION" -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION
        

    # Generate csr # - keytool nie generuje SANów
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -certreq -file $KAFKA_PATH/kafka$i.csr -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION 

    #openssl req -in $KAFKA_PATH/kafka$i.csr -noout -text 

    # Sign crt
    openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $KAFKA_PATH/kafka$i.csr -out $KAFKA_PATH/kafka$i.signed.crt -days $VALIDITY -CAcreateserial  \

    #openssl x509 -in $KAFKA_PATH/kafka$i.signed.crt -noout -text 

    # Add CA to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add signed cert to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -import -file $KAFKA_PATH/kafka$i.signed.crt -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add CA to truststore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.truststore.jks -alias CAroot -importcert -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    echo "confluent${i}" > $KAFKA_PATH/${i}_sslkey_creds        
    echo "confluent${i}" > $KAFKA_PATH/${i}_keystore_creds
    echo "confluent${i}" > $KAFKA_PATH/${i}_truststore_creds
done
