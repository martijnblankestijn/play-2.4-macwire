#!/usr/bin/env bash
sh src/test/docker/main.sh || exit;


export JDBC_DATABASE_URL='jdbc:postgresql://'$(docker-machine ip default)':5432/postgres?user=postgres&password=postgres'
sbt run