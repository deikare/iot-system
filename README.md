# iot-system

## Generowanie certyfikatów:

### Serwer
* `mkdir ./mounts` - stworzenie folderu służącego do montowania folderów z kontenerów w systemie
* `./ca-generate-certs.sh` - skrypt generujący CA - certyfikat oraz klucz CA, a także dodaje cert CA do truststore'a montowanego we wszystkich składnikach Kafki
* `./zoo-generate-certs.sh` - skrypt generujący certyfikat oraz klucz Zookeepera (procesów administrujących Kafką), a także tworzy keystore i truststore dla każdego node'a Zookeepera
* `./kafka-generate-certs.sh` - skrypt generujący certyfikaty oraz klucze brokerów Kafki, a także tworzy keystore i truststore dla każdego node'a Kafki
* `./traefik-generate-certs.sh` - skrypt generujący certyfikat oraz klucz Traefika
* `./curl-generate-certs.sh` - skrypt generujący certyfikat oraz klucz do uruchamiania curla - po uruchomieniu bazy można spingować bazę danych komendą `curl -v --cacert certs/ca/ca.crt --cert certs/curl/curl.crt --key certs/curl/curl.key -H Host:influxdb.docker.localhost https://localhost`

### Hub
* `./kafka-client-generate-certs.sh -c <ca-cert> -k <ca-key>` - skrypt generujący certyfikaty oraz klucze klienta Kafki, a także tworzy keystore i truststore
* `./telegraf-generate-certs.sh -c <ca-cert> -k <ca-key>` - skrypt generujący certyfikat oraz klucz Telegrafa


## Uruchamianie infrastruktury:
### Serwer 
* `docker-compose up -d zoo1 zoo2 zoo3` - uruchomienie Zookeeperów
* w oddzielnej konsoli `docker-compose logs -f` - uruchamia się w ten sposób proces wyświetlający logi wszystkich kontenerów
* `docker-compose up -d kafka1 kafka2 kafka3` - uruchomienie brokerów Kafki
* `docker-compose up -d reverse-proxy` - uruchomienie Traefika
* `docker-compose up -d influxdb` - uruchomienie bazy danych Influxdb
* `docker-compose up -d influxdb-init` - uruchomienie procesu inicjalizującego Influxdb
* `docker-compose up -d postgres` - uruchomienie procesu inicjalizującego Postgresql
* `docker-compose up -d backend` - uruchomienie serwera backend

### Hub
* `docker-compose up -d mqtt telegraf postgres` - uruchomienie Mosquitto, Telegrafa i Postgresql
* w oddzielnej konsoli `docker-compose logs -f` - uruchamia się w ten sposób proces wyświetlający logi wszystkich kontenerów
* `docker-compose up -d hub` - uruchomienie huba


### Przykładowe komendy
* `curl --cacert <cacert> --cert <cert> -- key <key> https://$SERVER_FQDN/backend/data`
* `curl --cacert <cacert> --cert <cert> -- key <key> https://$SERVER_FQDN/backend/logs`
* `curl --cacert <cacert> --cert <cert> -- key <key> -X POST https://$SERVER_FQDN/backend/send_hub_control/{hubId}?controlType={START|STOP}`
* `curl --cacert <cacert> --cert <cert> -- key <key> -X POST https://$SERVER_FQDN/backend/send_device_control/{controlSignalId}`
* `curl --cacert <cacert> --cert <cert> -- key <key> -X POST https://$SERVER_FQDN/backend/control_signals?deviceId={deviceId} -H 'Content-Type:application/json' -d '{"name": "xyz", "messageContent": "OPEN_GATE"}'`

