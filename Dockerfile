FROM inf-docker.fh-rosenheim.de/studwinten4338/docker-images/adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY ./build/libs/*.jar /opt/app
CMD ["java", "-jar", "/opt/app/messaging-1.0-SNAPSHOT.jar"]
