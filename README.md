# 🛍️ BarikaWeb — E-commerce & Blog Admin Dashboard

Bienvenue dans **BarikaWeb**, une application complète développée en **Spring Boot + Thymeleaf + MySQL**.
Elle permet la gestion d’un site e-commerce avec un espace administrateur sécurisé, un blog et un système d’authentification.

---

## 🚀 Fonctionnalités principales

✅ Authentification sécurisée (Spring Security)
✅ Espace administrateur (Dashboard)
✅ Gestion des produits et des articles
✅ Gestion des utilisateurs et des commandes
✅ Base de données MySQL
✅ Intégration Thymeleaf pour le front-end
✅ Architecture MVC (Model - View - Controller)

---

## ⚙️ 1️⃣ Installation du projet en local

### 🧩 Étape 1 — Cloner le projet
Ouvre ton terminal et exécute :
```bash
git clone https://github.com/JuniorMinkoSon/barikaweb.git
cd barikaweb
🧠 Étape 2 — Créer la base de données MySQL
Assure-toi que MySQL est bien lancé (via Laragon, WAMP, ou XAMPP).
Puis ouvre phpMyAdmin ou ton terminal MySQL et exécute le code suivant :

sql
Copier le code
-- 🔹 Création de la base
CREATE DATABASE ecom_blog;
USE ecom_blog;

-- 🔹 Table des utilisateurs
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE,
  nom VARCHAR(255),
  password VARCHAR(255),
  role VARCHAR(50)
);

-- 🔹 Table des articles du blog
CREATE TABLE article (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titre VARCHAR(255),
  contenu TEXT,
  image_url VARCHAR(255),
  categorie VARCHAR(100),
  date_publication DATETIME
);

-- 🔹 Table des produits
CREATE TABLE produit (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(255),
  description TEXT,
  categorie VARCHAR(255),
  prix DOUBLE,
  image_url VARCHAR(255),
  localisation VARCHAR(255),
  disponible BOOLEAN
);

-- 🔹 Table des commandes
CREATE TABLE commande (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  produit_id BIGINT,
  quantite INT,
  date_commande DATETIME,
  statut VARCHAR(50)
);

-- 🔹 Tables de session Spring Security
CREATE TABLE spring_session (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX spring_session_ix1 ON spring_session (SESSION_ID);
CREATE INDEX spring_session_ix2 ON spring_session (EXPIRY_TIME);
CREATE INDEX spring_session_ix3 ON spring_session (PRINCIPAL_NAME);

CREATE TABLE spring_session_attributes (
  SESSION_PRIMARY_ID CHAR(36) NOT NULL,
  ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
  ATTRIBUTE_BYTES BLOB NOT NULL,
  PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT spring_session_attributes_fk FOREIGN KEY (SESSION_PRIMARY_ID)
  REFERENCES spring_session (PRIMARY_ID) ON DELETE CASCADE
);

-- 🔹 Ajout d’un administrateur par défaut
INSERT INTO user (email, nom, password, role)
VALUES (
  'admin@barikaweb.com',
  'Administrateur',
  '$2a$10$Dow1nLr7xVwBEm90Lk6RceZo4PMBVXgS5aXoaZy.gdkv0l7gA5dSa',
  'ROLE_ADMIN'
);
⚙️ Étape 3 — Configurer la connexion MySQL
Dans le fichier :

css
Copier le code
src/main/resources/application.properties
Vérifie ou ajoute ceci :

properties
Copier le code
spring.datasource.url=jdbc:mysql://localhost:3306/ecom_blog
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.thymeleaf.cache=false
spring.security.user.roles=ADMIN
🧩 Étape 4 — Lancer le projet
Ouvre ton terminal à la racine du projet et exécute :

bash
Copier le code
mvn clean package
mvn spring-boot:run
🌍 Accès à l’application