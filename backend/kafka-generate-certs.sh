#!/bin/bash

CA_PATH="./certs/ca"
KAFKA_PATH="./certs/kafka"

if [ ! -d $KAFKA_PATH ] 
then
    mkdir $KAFKA_PATH
fi

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

STORETYPE="JKS"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
    CN_ITERATION=$CN$i

    SAN_ITERATION="dns:"$SAN$i,"dns:"$SERVER_FQDN

    STOREPASS_ITERATION="confluent"${i}
    KEYPASS_ITERATION="confluent"${i}

    # Create keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION, CN=$CN_ITERATION" -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION
        

    # Generate csr
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -certreq -file $KAFKA_PATH/kafka$i.csr -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    echo "[req]" > $KAFKA_PATH/kafka-cert.conf
    echo "distinguished_name = req_distinguished_name" >> $KAFKA_PATH/kafka-cert.conf
    echo "req_extensions = v3_req" >> $KAFKA_PATH/kafka-cert.conf
    echo "" >> $KAFKA_PATH/kafka-cert.conf

    echo "[req_distinguished_name]" >> $KAFKA_PATH/kafka-cert.conf
    echo "C = $COUNTRY" >> $KAFKA_PATH/kafka-cert.conf
    echo "ST = $STATE" >> $KAFKA_PATH/kafka-cert.conf
    echo "L = $LOCATION" >> $KAFKA_PATH/kafka-cert.conf
    echo "O = $ORGANIZATION" >> $KAFKA_PATH/kafka-cert.conf
    echo "CN = $CN_ITERATION" >> $KAFKA_PATH/kafka-cert.conf
    echo "" >> $KAFKA_PATH/kafka-cert.conf

    echo "[v3_req]" >> $KAFKA_PATH/kafka-cert.conf
    #echo "keyUsage = keyEncipherment, dataEncipherment" >> $KAFKA_PATH/kafka-cert.conf
    #echo "extendedKeyUsage = serverAuth" >> $KAFKA_PATH/kafka-cert.conf
    #echo "basicConstraints = critical,CA:false" >> $KAFKA_PATH/kafka-cert.conf
    echo "subjectAltName = @alt_names" >> $KAFKA_PATH/kafka-cert.conf
    echo "" >> $KAFKA_PATH/kafka-cert.conf

    echo "[alt_names]" >> $KAFKA_PATH/kafka-cert.conf
    echo "DNS.1 = $SAN$i" >> $KAFKA_PATH/kafka-cert.conf
    echo "DNS.2 = $SERVER_FQDN" >> $KAFKA_PATH/kafka-cert.conf
    
    # Sign crt
    openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $KAFKA_PATH/kafka$i.csr -out $KAFKA_PATH/kafka$i.signed.crt -days $VALIDITY -CAcreateserial  \
    -extfile $KAFKA_PATH/kafka-cert.conf -extensions v3_req

    # Add CA to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add signed cert to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -import -file $KAFKA_PATH/kafka$i.signed.crt -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add CA to truststore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.truststore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    echo "confluent${i}" > $KAFKA_PATH/${i}_sslkey_creds        
    echo "confluent${i}" > $KAFKA_PATH/${i}_keystore_creds
    echo "confluent${i}" > $KAFKA_PATH/${i}_truststore_creds

    rm $KAFKA_PATH/kafka$i.csr
    rm $KAFKA_PATH/kafka$i.signed.crt
    rm $KAFKA_PATH/kafka-cert.conf
done
