FROM openjdk:8-alpine
LABEL maintainer=rcbb

WORKDIR /apps

COPY gradle-demo.jar  ./

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Xmx256m" , "-jar" , "/apps/gradle-demo.jar"]
EXPOSE 7000