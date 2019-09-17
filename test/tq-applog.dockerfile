FROM tq/java:0.0.1

RUN apk add sqlite

ADD applogmanagement-0.0.1-SNAPSHOT.war applogmanagement-0.0.1-SNAPSHOT.war
ADD application.properties application.properties

EXPOSE 50051/tcp
EXPOSE 8080/tcp

CMD java -jar applogmanagement-0.0.1-SNAPSHOT.war