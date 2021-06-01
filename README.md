# iot-system

## Generowanie certyfikatów:

* `./ca-generate-certs.sh` - skrypt generujący CA - certyfikat oraz klucz CA, a także dodaje cert CA do truststore'a montowanego we wszystkich składnikach Kafki
* `./zoo-generate-certs.sh` - skrypt generujący certyfikat oraz klucz Zookeepera (procesów administrujących Kafką), a także tworzy keystore i truststore dla każdego node'a Zookeepera
* `./kafka-generate-certs.sh` - skrypt generujący certyfikaty oraz klucze brokerów Kafki, a także tworzy keystore i truststore dla każdego node'a Kafki
* `./kafka-client-generate-certs.sh` - skrypt generujący certyfikaty oraz klucze klientów Kafki, a także tworzy keystore i truststore dla każdego klienta
* `./traefik-generate-certs.sh` - skrypt generujący certyfikat oraz klucz Traefika
* `./curl-generate-certs.sh` - skrypt generujący certyfikat oraz klucz do uruchamiania curla - po uruchomieniu bazy można spingować bazę danych komendą `curl -v --cacert certs/ca/ca.crt --cert certs/curl/curl.crt --key certs/curl/curl.key -H Host:influxdb.docker.localhost https://localhost`

## Uruchamianie infrastruktury:

* `docker-compose up -d zoo1 zoo2 zoo3` - uruchomienie Zookeeperów
* w oddzielnej konsoli `docker-compose logs -f` - uruchamia się w ten sposób proces wyświetlający logi wszystkich kontenerów
* `docker-compose up -d kafka1 kafka2 kafka3` - uruchomienie brokerów Kafki
* `docker-compose up -d reverse-proxy` - uruchomienie Traefika
* `docker-compose up -d influxdb` - uruchomienie bazy danych Influxdb
* `docker-compose up -d influxdb-init` - uruchomienie procesu inicjalizującego Influxdb
