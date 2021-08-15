#!/bin/bash

CA_PATH="./certs/ca"
ZOOKEEPER_PATH="./certs/zoo"

if [ ! -d $ZOOKEEPER_PATH ] 
then
    mkdir $ZOOKEEPER_PATH
fi

rm $ZOOKEEPER_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="zoo"

CN="zoo"
SAN="zoo"

VALIDITY="365"
KEYALG="RSA"

STOREPASS="confluent"
KEYPASS="confluent"

STORETYPE="JKS"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
    CN_ITERATION=$CN$i

    SAN_ITERATION="dns:"$SAN$i

    STOREPASS_ITERATION="confluent"${i}
    KEYPASS_ITERATION="confluent"${i}

    # Create keystore
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION, CN=$CN_ITERATION" -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Generate csr
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -certreq -file $ZOOKEEPER_PATH/zoo$i.csr -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    echo "[req]" > $ZOOKEEPER_PATH/zoo-cert.conf
    echo "distinguished_name = req_distinguished_name" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "req_extensions = v3_req" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "" >> $ZOOKEEPER_PATH/zoo-cert.conf

    echo "[req_distinguished_name]" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "C = $COUNTRY" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "ST = $STATE" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "L = $LOCATION" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "O = $ORGANIZATION" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "CN = $CN_ITERATION" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "" >> $ZOOKEEPER_PATH/zoo-cert.conf

    echo "[v3_req]" >> $ZOOKEEPER_PATH/zoo-cert.conf
    #echo "keyUsage = keyEncipherment, dataEncipherment" >> $ZOOKEEPER_PATH/zoo-cert.conf
    #echo "extendedKeyUsage = serverAuth" >> $ZOOKEEPER_PATH/zoo-cert.conf
    #echo "basicConstraints = critical,CA:false" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "subjectAltName = @alt_names" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "" >> $ZOOKEEPER_PATH/zoo-cert.conf

    echo "[alt_names]" >> $ZOOKEEPER_PATH/zoo-cert.conf
    echo "DNS.1 = $SAN$i" >> $ZOOKEEPER_PATH/zoo-cert.conf    

    # Sign crt
    openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $ZOOKEEPER_PATH/zoo$i.csr -out $ZOOKEEPER_PATH/zoo$i.signed.crt -days $VALIDITY -CAcreateserial  \
    -extfile $ZOOKEEPER_PATH/zoo-cert.conf -extensions v3_req

    # Add CA to keystore
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.keystore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add signed cert to keystore
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -import -file $ZOOKEEPER_PATH/zoo$i.signed.crt -ext SAN=$SAN_ITERATION \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add CA to truststore
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.truststore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    #echo "confluent${i}" > $ZOOKEEPER_PATH/${i}_sslkey_creds        
    #echo "confluent${i}" > $ZOOKEEPER_PATH/${i}_keystore_creds
    #echo "confluent${i}" > $ZOOKEEPER_PATH/${i}_truststore_creds

    rm $ZOOKEEPER_PATH/zoo$i.csr
    rm $ZOOKEEPER_PATH/zoo$i.signed.crt
    rm $ZOOKEEPER_PATH/zoo-cert.conf
done