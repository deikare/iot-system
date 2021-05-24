#!/bin/bash

CA_PATH="./certs/ca"
TRAEFIK_PATH="./certs/traefik"

#rm $INFLUXDB_PATH/*

COUNTRY="PL"
STATE="Mazowieckie"
LOCATION="Warsaw"
ORGANIZATION="Warsaw University of Technology"
ORGANIZATION_UNIT="Warsaw University of Technology"

ALIAS="traefik"

CN="reverse-proxy"
SAN="traefik"
SERVER_FQDN="localhost"
DNS_EXTRA="whoami.docker.localhost"
DNS_EXTRA2="influxdb.docker.localhost"
SERVER_IP="127.0.0.1"

VALIDITY="365"
KEYALG="RSA"

echo "[req]" > $TRAEFIK_PATH/traefik-cert.conf
echo "distinguished_name = req_distinguished_name" >> $TRAEFIK_PATH/traefik-cert.conf
echo "req_extensions = v3_req" >> $TRAEFIK_PATH/traefik-cert.conf
echo "" >> $TRAEFIK_PATH/traefik-cert.conf

echo "[req_distinguished_name]" >> $TRAEFIK_PATH/traefik-cert.conf
echo "C = $COUNTRY" >> $TRAEFIK_PATH/traefik-cert.conf
echo "ST = $STATE" >> $TRAEFIK_PATH/traefik-cert.conf
echo "L = $LOCATION" >> $TRAEFIK_PATH/traefik-cert.conf
echo "O = $ORGANIZATION" >> $TRAEFIK_PATH/traefik-cert.conf
echo "CN = $CN" >> $TRAEFIK_PATH/traefik-cert.conf
echo "" >> $TRAEFIK_PATH/traefik-cert.conf

echo "[v3_req]" >> $TRAEFIK_PATH/traefik-cert.conf
echo "keyUsage = keyEncipherment, dataEncipherment" >> $TRAEFIK_PATH/traefik-cert.conf
echo "extendedKeyUsage = serverAuth" >> $TRAEFIK_PATH/traefik-cert.conf
echo "basicConstraints = critical,CA:false" >> $TRAEFIK_PATH/traefik-cert.conf
echo "subjectAltName = @alt_names" >> $TRAEFIK_PATH/traefik-cert.conf
echo "" >> $TRAEFIK_PATH/traefik-cert.conf

echo "[alt_names]" >> $TRAEFIK_PATH/traefik-cert.conf
echo "DNS.1 = $SAN" >> $TRAEFIK_PATH/traefik-cert.conf  
echo "DNS.2 = $SERVER_FQDN" >> $TRAEFIK_PATH/traefik-cert.conf 
echo "DNS.3 = $DNS_EXTRA" >> $TRAEFIK_PATH/traefik-cert.conf 
echo "IP.1 = $SERVER_IP" >> $TRAEFIK_PATH/traefik-cert.conf 


openssl req -newkey rsa:2048 -nodes -keyout $TRAEFIK_PATH/traefik.key \
    -subj "/C=$COUNTRY/ST=$STATE/L=$LOCATION/O=$ORGANIZATION/OU=$ORGANIZATION_UNIT/CN=$CN" -out $TRAEFIK_PATH/traefik.csr

openssl x509 -req -CA $CA_PATH/ca.crt -CAkey $CA_PATH/ca.key -in $TRAEFIK_PATH/traefik.csr -out $TRAEFIK_PATH/traefik.crt -days $VALIDITY -CAcreateserial  \
    -extfile $TRAEFIK_PATH/traefik-cert.conf -extensions v3_req

rm $TRAEFIK_PATH/traefik.csr
rm $TRAEFIK_PATH/traefik-cert.conf

cp $CA_PATH/ca.crt $TRAEFIK_PATH/ca.crt