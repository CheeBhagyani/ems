FROM openjdk:8
EXPOSE 9091
ADD target/auth-server.jar auth-server.jar
ENTRYPOINT ["java", "-jar", "/auth-server.jar"]