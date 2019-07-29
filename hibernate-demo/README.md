# Hibernate Postgresql demo
Inspired by [this](https://dzone.com/articles/hibernate-5-java-configuration-example) tutorial.
This is simple demo of Hibernate ORM in Java standalone environment.

## Example application
Example application ``itx.examples.hibernate.App`` is designed to run against real postgresql server. 
Please install and start postgresql server before running the App, following guide below.

## Unit tests 
Unit tests are designed to run with in-memory apache derby database acting like real SQL persistence database.

## Build and run
```gradle clean build test```

### Install Postresql
Download [binary distribution](https://www.enterprisedb.com/download-postgresql-binaries) 
of [postresql](https://www.postgresql.org/) and unzip it into ``/opt/postresql``

#### Setup server
```
cd /opt/postgresql
./bin/initdb /opt/postgresql/data
# start server
./bin/pg_ctl -D /opt/postgresql/data -l /opt/postgresql/logfile start
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
GRANT ALL PRIVILEGES ON DATABASE userdata TO username;

# select data from userdata database and table userdata
\c userdata;
select * from userdata;
```

#### Stop server
```./bin/pg_ctl -D /opt/postgresql/data -l /opt/postgresql/logfile stop```
