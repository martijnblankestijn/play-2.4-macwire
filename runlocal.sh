#!/usr/bin/env bash
sh src/test/docker/main.sh || exit;


export DATABASE_JDBC_FORMAT_URL='jdbc:postgresql://'$(docker-machine ip default)':5432/postgres?user=postgres&password=postgres'
sbt run