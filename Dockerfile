# === Étape 1 : Construire le projet ===
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copier les fichiers Maven et construire le projet
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# === Étape 2 : Lancer l'application ===
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copier le JAR compilé depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Définir le port d’écoute
EXPOSE 8081

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
