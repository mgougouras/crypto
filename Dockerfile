FROM openjdk:17.0.1-slim

USER root

# Default environment variables. Could be overridden.
ENV HTTP_SERVER_PORT=8080
ENV HTTP_SERVER_MANAGEMENT_PORT=9999

RUN mkdir /applications

RUN apt update && apt install -y libnss3 curl wget unzip ca-certificates && apt autoremove -y && apt clean

ARG artifactVersion

COPY ./build/libs/crypto-${artifactVersion}.jar /applications/service.jar
COPY ./build/resources/main/bootstrap.yml /applications
COPY ./build/resources/main/log4j2.json /applications/log4j2.json
COPY ./build/resources/main/run-java.sh /applications

RUN chmod 755 /applications/run-java.sh

WORKDIR /applications
CMD ["/applications/run-java.sh"]
