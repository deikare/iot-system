#!/bin/bash

while getopts "c:k:" option; do
  case $option in
    c ) CA_CERT=$OPTARG
    ;;
    k ) CA_KEY=$OPTARG
    ;;
  esac
done

CA_PATH="./certs/ca"
TELEGRAF_PATH="./certs/telegraf"

if [ ! -d $TELEGRAF_PATH ] 
then
    mkdir $TELEGRAF_PATH
fi

rm $TELEGRAF_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

CN="telegraf"

VALIDITY="365"

openssl req -newkey rsa:2048 -nodes -keyout $TELEGRAF_PATH/telegraf.key \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $TELEGRAF_PATH/telegraf.csr

openssl x509 -req -CA $CA_CERT -CAkey $CA_KEY -in $TELEGRAF_PATH/telegraf.csr -out $TELEGRAF_PATH/telegraf.crt -days $VALIDITY -CAcreateserial

rm $TELEGRAF_PATH/telegraf.csr

cp $CA_CERT $TELEGRAF_PATH/ca.crt