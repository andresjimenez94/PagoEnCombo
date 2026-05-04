# Etapa 1: Construcción con Gradle y JDK 21
FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Usamos --no-daemon para ahorrar memoria en Render
RUN gradle build --no-daemon -x test

# Etapa 2: Ejecución con JRE 21
FROM eclipse-temurin:21-jre-jammy
# El asterisco asegura que tome el JAR generado, pero filtramos el 'plain' en el nombre si es necesario
COPY --from=build /home/gradle/src/build/libs/*[0-9].jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
