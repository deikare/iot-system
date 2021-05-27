#!/bin/bash

CA_PATH="./certs/ca"
CURL_PATH="./certs/curl"

rm $CURL_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

CN="curl"

VALIDITY="365"

openssl req -newkey rsa:2048 -nodes -keyout $CURL_PATH/curl.key \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $CURL_PATH/curl.csr

openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $CURL_PATH/curl.csr -out $CURL_PATH/curl.crt -days $VALIDITY -CAcreateserial

rm $CURL_PATH/curl.csr