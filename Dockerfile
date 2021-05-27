FROM maven AS build  
WORKDIR /usr/src/app
COPY src /usr/src/app/src
#COPY lib /usr/src/app/lib  
COPY pom.xml /usr/src/app
#COPY src/IP/app.config /usr/src/app/src/IP

RUN mvn -f /usr/src/app/pom.xml clean install -DskipTests
CMD mvn exec:java -D exec.mainClass=IP.Server.RestServer
EXPOSE 8000
