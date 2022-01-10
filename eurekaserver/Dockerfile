FROM openjdk:11-slim as build

LABEL maintainer="Andrei H <andreih1981@gmail.com>"

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

FROM openjdk:11-slim

VOLUME /tmp
#RUN apk update && apk upgrade && apk add netcat-openbsd

ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.configserver.EurekaserverApplication"]
