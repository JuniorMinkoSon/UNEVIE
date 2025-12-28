@echo off
REM Script pour réinitialiser la base de données avec les données de test

echo ========================================
echo   UNEVIE - Réinitialisation BD
echo ========================================
echo.

echo Cette opération va:
echo  1. Supprimer toutes les données
echo  2. Recréer les tables
echo  3. Insérer les données de test
echo.

set /p confirm="Continuer? (O/N): "
if /i not "%confirm%"=="O" (
    echo Opération annulée
    pause
    exit /b 0
)

echo.
echo [1/3] Connexion à PostgreSQL...

REM Exécuter le script SQL
psql -U postgres -d ecom_blog -f src\main\resources\data-init.sql

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Erreur lors de l'exécution du script
    echo Vérifiez:
    echo  - PostgreSQL est démarré
    echo  - La base 'ecom_blog' existe
    echo  - Les identifiants sont corrects
    pause
    exit /b 1
)

echo.
echo ========================================
echo ✅ Initialisation terminée!
echo ========================================
echo.
echo Données créées:
echo  - 5 Quartiers
echo  - 7 Entreprises
echo  - 10 Produits/Services
echo.
echo 🌐 Démarrez l'application avec:
echo    mvn spring-boot:run
echo.
echo Puis accédez à:
echo    http://localhost:8082/entreprises
echo ========================================

pause
