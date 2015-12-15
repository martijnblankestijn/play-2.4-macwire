#!/usr/bin/env bash

# working directory for script
thisDir=`dirname $0`

# Clean up running containers
docker rm -f postgres 2> /dev/null
rm -f ${thisDir}/CONTID

docker build -t mblankestijn/postgres ${thisDir}
retval=$?
if [ $retval -eq 0 ]
then
  echo "Docker image created"
else
  echo "Error building Docker image, return code is " $retval;
  echo "Maybe docker machine is not running and you should run:"
  echo '  docker-machine start default && eval "$(docker-machine env default)"'
  exit $retval;
fi

SCRIPT_PATH=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

docker run -d -p 5432:5432 --cidfile=${thisDir}/CONTID -v $SCRIPT_PATH/sql:/docker-entrypoint-initdb.d --name postgres mblankestijn/postgres

# Postgres does not start instantaneously, so we have to wait a bit before prepping the DB
while [[ $VERSION != *PostgreSQL* ]]
do
    echo "Sleeping";
    sleep 2;
    VERSION=$(docker exec postgres psql -h $(docker-machine ip default) -U postgres -c 'SELECT version()')
done

echo "postgres ready"
