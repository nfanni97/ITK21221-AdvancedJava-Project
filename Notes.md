# Postgre setup

0. in Project directory
1. create db: `initdb -D db -U fanni -W -E UTF8 -A scram-sha-256`
   1. pass: fanni
2. modify db/postgresql.conf/unix_socket_directories to the location of db directory
3. start server: `pg_ctl -D ./db -l logfile start`
4. create database: `createdb -h <socket directory> registryDB`
5. stop: `pg_ctl -D ./db stop`

# Tests

- unitTest: categoryController/modifyCategory
- integration: categoryRepo/findByName
- [source](https://www.baeldung.com/spring-boot-testing)

# Authentication source

[link](https://www.bezkoder.com/spring-boot-security-postgresql-jwt-authentication/#Overview)

[refresh-token](https://www.bezkoder.com/spring-boot-refresh-token-jwt/)
