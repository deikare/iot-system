#!/bin/bash

CA_PATH="./certs/ca"
INFLUXDB_PATH="./certs/influxdb"
INFLUXDB_DOCKERFILES_PATH="./Dockerfiles"

rm $INFLUXDB_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="influxdb"

CN="database"
SAN="influxdb"
SERVER_FQDN="localhost"

VALIDITY="365"
KEYALG="RSA"

echo "[req]" > $INFLUXDB_PATH/influxdb-cert.conf
echo "distinguished_name = req_distinguished_name" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "req_extensions = v3_req" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "" >> $INFLUXDB_PATH/influxdb-cert.conf

echo "[req_distinguished_name]" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "C = $COUNTRY" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "ST = $STATE" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "L = $LOCATION" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "O = $ORGANIZATION" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "CN = $CN" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "" >> $INFLUXDB_PATH/influxdb-cert.conf

echo "[v3_req]" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "keyUsage = keyEncipherment, dataEncipherment" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "extendedKeyUsage = serverAuth" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "basicConstraints = critical,CA:false" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "subjectAltName = @alt_names" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "" >> $INFLUXDB_PATH/influxdb-cert.conf

echo "[alt_names]" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "DNS.1 = $SAN" >> $INFLUXDB_PATH/influxdb-cert.conf  
echo "DNS.2 = $SERVER_FQDN" >> $INFLUXDB_PATH/influxdb-cert.conf
echo "DNS.3 = influxdb.docker.localhost" >> $INFLUXDB_PATH/influxdb-cert.conf 


openssl req -newkey rsa:2048 -nodes -keyout $INFLUXDB_PATH/influxdb.key \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $INFLUXDB_PATH/influxdb.csr

openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $INFLUXDB_PATH/influxdb.csr -out $INFLUXDB_PATH/influxdb.crt -days $VALIDITY -CAcreateserial  \
    -extfile $INFLUXDB_PATH/influxdb-cert.conf -extensions v3_req

rm $INFLUXDB_PATH/influxdb.csr
rm $INFLUXDB_PATH/influxdb-cert.conf

cp $INFLUXDB_PATH/influxdb.crt $INFLUXDB_DOCKERFILES_PATH/certs/influxdb.crt
cp $INFLUXDB_PATH/influxdb.key $INFLUXDB_DOCKERFILES_PATH/certs/influxdb.key
cp $CA_PATH/ca.crt $INFLUXDB_DOCKERFILES_PATH/certs/ca.crt
