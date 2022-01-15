#!/bin/bash
function unpack() {
   FOLDER=$1
   NAME=$2
   VERSION=$3

   CURRENT=$(pwd)

   cd $FOLDER/target
   java -jar -Djarmode=layertools ${NAME}-${VERSION}.jar extract

   cd $CURRENT
}

function build() {
   FOLDER=$1
   NAME=$2
   VERSION=$3

   docker build -f ./docker/layered/Dockerfile \
     --build-arg JAR_FOLDER=${FOLDER}/target \
     -t ${NAME}:${VERSION} .
}

APP_VERSION=0.0.1-SNAPSHOT

cd ..
mvn clean package -DskipTests

echo "Unpacking JARs"
unpack configserver configserver ${APP_VERSION}
unpack eurekaserver eurekaserver ${APP_VERSION}
unpack licensing-service licensing-service ${APP_VERSION}
unpack organizationservice organization-service ${APP_VERSION}

echo "Building Docker images"

build configserver ostock/configserver ${VERSION}
build eurekaserver ostock/eurekaserver ${VERSION}
build licensing-service ostock/licensing-service ${VERSION}
build organizationservice ostock/organization-service ${VERSION}
