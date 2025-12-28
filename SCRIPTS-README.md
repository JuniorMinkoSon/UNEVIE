# 🚀 Scripts UNEVIE

## Scripts Disponibles

### 1. `start-unevie.bat` - Démarrage Complet

Lance l'application avec vérifications :
```bash
start-unevie.bat
```

**Ce qu'il fait :**
- Vérifie PostgreSQL
- Nettoie le projet (mvn clean)
- Compile (mvn compile)
- Démarre l'application (mvn spring-boot:run)

### 2. `reset-database.bat` - Réinitialiser la BD

Réinitialise la base de données avec les données de test :
```bash
reset-database.bat
```

**Ce qu'il fait :**
- Supprime toutes les données
- Recrée les tables
- Insère 5 quartiers + 7 entreprises + 10 produits

### 3. `data-init.sql` - Script SQL Brut

Script SQL pour initialisation manuelle :
```bash
psql -U postgres -d ecom_blog -f src/main/resources/data-init.sql
```

## Utilisation Rapide

### Méthode 1 : Automatique (Recommandé)

L'application a déjà le `DataInitializerService` qui crée les données automatiquement !

```bash
mvn spring-boot:run
```

Les données sont créées au démarrage.

### Méthode 2 : Avec Script

```bash
# 1. Exécuter le script de démarrage
start-unevie.bat

# 2. Ouvrir le navigateur
http://localhost:8082/entreprises
```

### Méthode 3 : Réinitialisation Manuelle

Si vous voulez réinitialiser manuellement :

```bash
# 1. Réinitialiser la base
reset-database.bat

# 2. Démarrer l'app (sans recréer les données)
# Temporairement désactiver DataInitializerService
mvn spring-boot:run
```

## Données Créées

Peu importe la méthode, vous aurez :

**Quartiers (5) :**
- Cocody
- Yopougon  
- Plateau
- Marcory
- Treichville

**Entreprises (7) :**

| Nom | Catégorie | Quartier |
|-----|-----------|----------|
| Babi Location | Véhicules | Cocody |
| Sponguy Location | Véhicules | Yopougon |
| Résidences Premium CI | Résidences | Plateau |
| Assinie Beach Resort | Résidences | Cocody |
| Délices d'Afrique | Traiteur | Marcory |
| Royal Catering Services | Traiteur | Treichville |
| EventMakers Pro | Événements | Cocody |

**Produits (10) :**
- 3 véhicules (Babi Location)
- 2 véhicules (Sponguy Location)
- 3 logements (Résidences Premium CI)
- 2 villas (Assinie Beach Resort)

## Résolution de Problèmes

### "PostgreSQL n'est pas accessible"

Vérifiez :
```bash
# Tester la connexion
psql -U postgres -d ecom_blog -c "SELECT version();"

# Si ça ne marche pas :
# 1. PostgreSQL est-il démarré ?
# 2. La base ecom_blog existe ?
# 3. Le mot de passe est 'postgres' ?
```

### "Les données ne s'affichent pas"

1. Vérifiez les logs au démarrage
2. Cherchez le message : "🎉 Initialisation terminée !"
3. Si absent, `DataInitializerService` n'a pas tourné

### "Erreur de compilation"

```bash
# Nettoyer complètement
mvn clean

# Recompiler
mvn compile

# Redémarrer
mvn spring-boot:run
```

## Notes

- `spring.jpa.hibernate.ddl-auto=create` recrée la BD à chaque démarrage
- Les données de test sont toujours réinsérées
- Changez en `update` ou `validate` en production
