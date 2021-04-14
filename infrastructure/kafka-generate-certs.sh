#!/bin/bash

PATH="certs/kafka"

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
    /usr/bin/keytool -keystore $PATH/kafka.server$i.keystore.jks -alias $ALIAS$i -validity $VALIDITY -genkey -keyalg $KEYALG -dname "C=$COUNTRY, ST=$STATE, L=$LOCATION, O=$ORGANIZATION_UNIT, CN=kafka$i" -ext SAN=DNS:$SAN$i
    /usr/bin/keytool -list -v -keystore $PATH/kafka.server$i.keystore.jks
    /usr/bin/keytool -keystore kafka.server.truststore.jks -alias CA -importcert -file $PATH/ca.crt
    /usr/bin/keytool -keystore kafka.server.keystore.jks -alias $ALIAS$i -certreq -file cert-file
done
