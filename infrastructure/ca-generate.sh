#!/bin/bash

CA_PATH="./certs/ca"
JRE_CUSTOM_CERTS_PATH="./certs/jre-custom-certs"

if [ ! -d $CA_PATH ] 
then
    mkdir $CA_PATH
fi

rm $CA_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

CN="CA"

VALIDITY="365"

ALIAS=`openssl rand -base64 12` #some random string for alias
STOREPASS="changeit"

openssl genrsa -des3 -out $CA_PATH/ca.key 4096

openssl req -x509 -new -nodes -key $CA_PATH/ca.key -sha256 -days $VALIDITY \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $CA_PATH/ca.crt

keytool -import -keystore $JRE_CUSTOM_CERTS_PATH/cacerts -alias $ALIAS -file $CA_PATH/ca.crt -storepass $STOREPASS
