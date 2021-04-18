#!/bin/bash

<<<<<<< HEAD
CA_PATH="certs"
KAFKA_PATH="certs/kafka"
=======
path="certs"
>>>>>>> parent of 23e6b25... Kafka is almost done, only custom hosts are needed

rm $path/kafka/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="kafka"
<<<<<<< HEAD
SAN="DNS:localhost"
CN="kafka"
=======
SAN="kafka"
>>>>>>> parent of 23e6b25... Kafka is almost done, only custom hosts are needed
VALIDITY="365"
KEYALG="RSA"

STOREPASS="confluent"
KEYPASS="confluent"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
<<<<<<< HEAD
    CN_ITERATION=$CN$i

    STOREPASS_ITERATION="confluent"${i}
    KEYPASS_ITERATION="confluent"${i}

    # Create .conf file for proper openssl signing
    echo "[req]" > $KAFKA_PATH/server$i.conf
    echo "distinguished_name = req_distinguished_name" >> $KAFKA_PATH/server$i.conf
    echo "req_extensions = v3_req" >> $KAFKA_PATH/server$i.conf
    echo "prompt = no" >> $KAFKA_PATH/server$i.conf
    echo -e "" >> $KAFKA_PATH/server$i.conf

    echo "[req_distinguished_name]" >> $KAFKA_PATH/server$i.conf
    echo "C = $COUNTRY" >> $KAFKA_PATH/server$i.conf
    echo "ST = $STATE" >> $KAFKA_PATH/server$i.conf
    echo "L = $LOCATION" >> $KAFKA_PATH/server$i.conf
    echo "O = $ORGANIZATION" >> $KAFKA_PATH/server$i.conf
    echo "prompt = no" >> $KAFKA_PATH/server$i.conf
    echo "CN = $CN" >> $KAFKA_PATH/server$i.conf
    echo -e "" >> $KAFKA_PATH/server$i.conf

    echo "[v3_req]" >> $KAFKA_PATH/server$i.conf
    echo "keyUsage = keyEncipherment, dataEncipherment" >> $KAFKA_PATH/server$i.conf
    echo "extendedKeyUsage = serverAuth" >> $KAFKA_PATH/server$i.conf
    echo "subjectAltName = $SAN" >> $KAFKA_PATH/server$i.conf
    echo "[alt_names]" >> $KAFKA_PATH/server$i.conf
    echo "DNS.1 = $CN_ITERATION" >> $KAFKA_PATH/server$i.conf
    echo "DNS.2 = localhost" >> $KAFKA_PATH/server$i.conf

    # Create keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION, CN=$CN_ITERATION" -ext SAN=$SAN \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION
        

    # Generate csr # - keytool nie generuje SANów
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -certreq -file $KAFKA_PATH/kafka$i.csr -ext SAN=$SAN \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION 

    openssl req -in $KAFKA_PATH/kafka$i.csr -noout -text 

    # Sign crt
    openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $KAFKA_PATH/kafka$i.csr -out $KAFKA_PATH/kafka$i.signed.crt -days $VALIDITY -CAcreateserial  \
        -extfile $KAFKA_PATH/server$i.conf -extensions v3_req

    openssl x509 -in $KAFKA_PATH/kafka$i.signed.crt -noout -text 

    # Add CA to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias CAroot -import -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add signed cert to keystore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.keystore.jks -alias $ALIAS$i -import -file $KAFKA_PATH/kafka$i.signed.crt -ext SAN=$SAN \
        -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    # Add CA to truststore
    keytool -keystore $KAFKA_PATH/kafka.broker$i.truststore.jks -alias CAroot -importcert -file $CA_PATH/ca.crt -storepass $STOREPASS_ITERATION -keypass $KEYPASS_ITERATION

    echo "confluent${i}" > $KAFKA_PATH/${i}_sslkey_creds        
    echo "confluent${i}" > $KAFKA_PATH/${i}_keystore_creds
    echo "confluent${i}" > $KAFKA_PATH/${i}_truststore_creds
=======
    #echo $'\nCreate keystores'
    keytool -keystore $path/kafka/kafka.server$i.keystore.jks -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG \
        -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION_UNIT, CN=kafka$i" -ext SAN=DNS:$SAN$i \
        -storepass confluent -keypass confluent

    #echo "Generate csr"
    keytool -keystore $path/kafka/kafka.server$i.keystore.jks -alias $ALIAS$i -certreq -file $path/kafka/kafka$i.crt -storepass confluent -keypass confluent

    #echo "Sign crt"
    openssl x509 -req -CA $path/ca.crt -CAkey $path/ca.key -in $path/kafka/kafka$i.crt -out $path/kafka/kafka$i.signed.crt -days $VALIDITY -CAcreateserial 

    #echo "Add CA to keystore"
    keytool -keystore $path/kafka.server$i.keystore.jks -alias CA -import -file $path/ca.crt -storepass confluent -keypass confluent

    #echo "Add signed cert to keystore"
    keytool -keystore $path/kafka.server$i.keystore.jks -alias $path/kafka/kafka$i.crt -import -file $path/kafka/kafka$i.signed.crt -storepass confluent -keypass confluent

    #echo "Add CA to truststore"
    keytool -keystore $path/kafka/kafka.server$i.truststore.jks -alias CA -importcert -file $path/ca.crt -storepass confluent -keypass confluent
>>>>>>> parent of 23e6b25... Kafka is almost done, only custom hosts are needed
done
