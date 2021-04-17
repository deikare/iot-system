#!/bin/bash

path="certs"

rm $path/kafka/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="kafka"
SAN="kafka"
VALIDITY="365"
KEYALG="RSA"

NODES_NUMBER=3

for ((i=1;i<=NODES_NUMBER;i++)); do
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
done
