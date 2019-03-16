FROM openjdk:8-jre-alpine

ADD target/control-app.jar /

CMD ["java", "-Dvertx.cacheDirBase=/tmp", "-Dvertx.disableDnsResolver=true", "-jar", "control-app.jar"]