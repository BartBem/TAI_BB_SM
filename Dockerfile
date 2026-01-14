# ============================================
# DOCKERFILE - Wypożyczalnia Filmów
# ============================================
# Ten plik instruuje Dockera jak zbudować obraz aplikacji.
# 
# Multi-stage build:
# 1. ETAP BUILD: kompiluje aplikację (używa Maven)
# 2. ETAP RUN: uruchamia skompilowany JAR (lekki obraz)

# ============================================
# ETAP 1: BUILD (kompilacja)
# ============================================
FROM eclipse-temurin:17-jdk AS build

# Katalog roboczy w kontenerze
WORKDIR /app

# Kopiuj pliki Maven (najpierw - dla cache'owania)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Nadaj uprawnienia do uruchomienia mvnw
RUN chmod +x mvnw

# Pobierz zależności (cache'owane jeśli pom.xml się nie zmieni)
RUN ./mvnw dependency:go-offline -B

# Kopiuj kod źródłowy
COPY src src

# Zbuduj aplikację (bez testów dla szybkości)
RUN ./mvnw package -DskipTests

# ============================================
# ETAP 2: RUN (uruchomienie)
# ============================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Kopiuj zbudowany JAR z etapu BUILD
COPY --from=build /app/target/*.jar app.jar

# Port na którym działa aplikacja
EXPOSE 8080

# Komenda uruchamiająca aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]
