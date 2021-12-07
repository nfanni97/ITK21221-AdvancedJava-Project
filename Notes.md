# Postgre setup

0. in Project directory
1. create db: `initdb -D db -U fanni -W -E UTF8 -A scram-sha-256`
   1. pass: fanni
2. modify db/postgresql.conf/unix_socket_directories to the location of db directory
3. start server: `pg_ctl -D ./db -l logfile start`
4. create database: `createdb -h <socket directory> registryDB`
5. stop: `pg_ctl -D ./db stop`

# Authentication source

[link](https://www.bezkoder.com/spring-boot-security-postgresql-jwt-authentication/#Overview)

# Demo

- signup: `curl -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}'`
- login: `curl -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}' > temp.log`
  - `export TOKEN=$(cat temp.log | jq -r '.token')`
- get dummy content: `curl -X GET localhost:8080/api/content/hello -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"`