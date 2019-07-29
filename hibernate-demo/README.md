# Hibernate Postgresql demo
Inspired by [this](https://dzone.com/articles/hibernate-5-java-configuration-example) tutorial.

### Install Postresql
Download [binary distribution](https://www.enterprisedb.com/download-postgresql-binaries) 
of [postresql](https://www.postgresql.org/) and unzip it into ``/opt/postresql``
```
cd /opt/postgresql
./bin/initdb /opt/postgresql/data
# start server
./bin/pg_ctl -D /opt/postgresql/data -l /opt/postgresql/logfile start
# stop server
./bin/pg_ctl -D /opt/postgresql/data -l /opt/postgresql/logfile stop
```
Configure database server access. In ``data/postgresql.conf``, set 
```
listen_addresses = '*'
```
In ``data/pg_hba.conf`` add line
```
host all all 0.0.0.0/0 md5
host all all      ::/0 md5
```
Create user to access the database
```
./createdb username
./psql -U username
\password
# set password for username
SELECT * FROM pg_catalog.pg_tables;
# create database userdata
create database userdata;
```
