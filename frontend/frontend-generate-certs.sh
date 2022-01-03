#!/bin/bash

CERTS_PATH="./certs"

while getopts "c:k:" option; do
  case $option in
    c ) CA_CERT=$OPTARG
    ;;
    k ) CA_KEY=$OPTARG
    ;;
  esac
done

if [ ! -d $CERTS_PATH ] 
then
    mkdir $CERTS_PATH
fi

rm $CERTS_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

CN="frontend"

VALIDITY="365"

openssl req -newkey rsa:2048 -nodes -keyout $CERTS_PATH/frontend.key \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $CERTS_PATH/frontend.csr

openssl x509 -req -CA $CA_CERT -CAkey $CA_KEY -in $CERTS_PATH/frontend.csr -out $CERTS_PATH/frontend.crt -days $VALIDITY -CAcreateserial

openssl pkcs12 -export -out $CERTS_PATH/frontend.pfx -inkey $CERTS_PATH/frontend.key -in $CERTS_PATH/frontend.crt

rm $CERTS_PATH/frontend.csr
# rm $CERTS_PATH/frontend.key
# rm $CERTS_PATH/frontend.crt

cp $CA_CERT $CERTS_PATH/ca.crt