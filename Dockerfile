FROM java:8
ADD target/*.jar fuchuang2.jar
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fuchuang2.jar"]
