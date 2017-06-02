#!/bin/bash

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	CREATE USER "service.dog" WITH PASSWORD 'service.dog@BwUbMQh401';
	CREATE USER "sync.dog" WITH PASSWORD 'sync.dog@WxG8numYI3';
	CREATE USER "store.dog" WITH PASSWORD 'store.dog@Gwx6TfTW63';
	CREATE DATABASE "ask.dog";
	CREATE DATABASE "ask.dog.sync";
	CREATE DATABASE "ask.dog.store";
    GRANT ALL PRIVILEGES ON DATABASE "ask.dog" TO "service.dog";
	GRANT ALL PRIVILEGES ON DATABASE "ask.dog.sync" TO "sync.dog";
	GRANT ALL PRIVILEGES ON DATABASE "ask.dog.store" TO "store.dog";
EOSQL