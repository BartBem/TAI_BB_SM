# Etap 1: Budowanie aplikacji (Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Budujemy aplikację bez uruchamiania testów (szybciej)
RUN mvn clean package -DskipTests

# Etap 2: Uruchamianie aplikacji (JDK)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Port wewnętrzny kontenera
EXPOSE 8080

# Uruchomienie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
