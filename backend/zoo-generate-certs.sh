#!/bin/bash

CA_PATH="./certs/ca"
ZOOKEEPER_PATH="./certs/zoo"
VOLUMES_PATH="./mounts/zoo"

if [ ! -d $ZOOKEEPER_PATH ] 
then
    mkdir $ZOOKEEPER_PATH
fi

rm $ZOOKEEPER_PATH/*

if [ ! -d $VOLUMES_PATH ] 
then
    mkdir $VOLUMES_PATH
fi

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
    if [ ! -d $VOLUMES_PATH/$i ] 
    then
        mkdir $VOLUMES_PATH/$i
    fi

    sudo chown -R 1000:1000 $VOLUMES_PATH/$i

    if [ ! -d $VOLUMES_PATH/$i/data ] 
    then
        mkdir $VOLUMES_PATH/$i/data
    fi

    sudo chown -R 1000:1000 $VOLUMES_PATH/$i/data

    if [ ! -d $VOLUMES_PATH/$i/logs ] 
    then
        mkdir $VOLUMES_PATH/$i/logs
    fi

    sudo chown -R 1000:1000 $VOLUMES_PATH/$i/logs
    

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
    echo "keyUsage = keyEncipherment, dataEncipherment, digitalSignature" >> $KAFKA_PATH/kafka-cert.conf
    echo "extendedKeyUsage = serverAuth, clientAuth" >> $KAFKA_PATH/kafka-cert.conf
    echo "basicConstraints = CA:false" >> $KAFKA_PATH/kafka-cert.conf
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
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.keystore.jks -storetype $STORETYPE -alias $ALIAS$i -import -file $ZOOKEEPER_PATH/zoo$i.signed.crt -ext SAN=$SAN_ITERATION -ext keyUsage=keyEncipherment,dataEncipherment,digitalSignature -ext extendedKeyUsage=serverAuth,clientAuth \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add CA to truststore
    keytool -keystore $ZOOKEEPER_PATH/zoo$i.truststore.jks -storetype $STORETYPE -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    rm $ZOOKEEPER_PATH/zoo$i.csr
    rm $ZOOKEEPER_PATH/zoo$i.signed.crt
    rm $ZOOKEEPER_PATH/zoo-cert.conf
done
