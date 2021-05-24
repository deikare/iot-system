#!/bin/bash

CA_PATH="./certs/ca"
JRE_CUSTOM_CERTS_PATH="./certs/jre-custom-certs"

rm $CA_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

CN="CA"

VALIDITY="365"

ALIAS="CAroot"

openssl genrsa -des3 -out $CA_PATH/ca.key 4096

openssl req -x509 -new -nodes -key $CA_PATH/ca.key -sha256 -days $VALIDITY \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $CA_PATH/ca.crt

keytool -import -keystore $JRE_CUSTOM_CERTS_PATH/cacerts -file $CA_PATH/ca.crt
