@echo off
REM =====================================================
REM UNEVIE - Script d'initialisation et démarrage
REM =====================================================

echo.
echo ========================================
echo   UNEVIE - Initialisation Complète
echo ========================================
echo.

REM Vérifier si PostgreSQL est accessible
echo [1/4] Vérification de PostgreSQL...
psql -U postgres -d ecom_blog -c "SELECT version();" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ PostgreSQL n'est pas accessible. Assurez-vous que:
    echo    - PostgreSQL est installé et démarré
    echo    - La base 'ecom_blog' existe
    echo    - L'utilisateur 'postgres' a les droits
    pause
    exit /b 1
)
echo ✅ PostgreSQL OK

REM Nettoyer la compilation
echo.
echo [2/4] Nettoyage du projet...
call mvn clean -q
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Erreur lors du nettoyage
    pause
    exit /b 1
)
echo ✅ Nettoyage terminé

REM Compiler le projet
echo.
echo [3/4] Compilation du projet...
call mvn compile -q
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Erreur de compilation
    pause
    exit /b 1
)
echo ✅ Compilation réussie

REM Démarrer l'application
echo.
echo [4/4] Démarrage de l'application...
echo.
echo ========================================
echo   L'application démarre...
echo ========================================
echo.
echo 🌐 Une fois démarré, accédez à:
echo    http://localhost:8082/entreprises
echo.
echo 📝 Données de test créées automatiquement:
echo    - 5 quartiers (Cocody, Yopougon, etc.)
echo    - 7 entreprises (Babi Location, etc.)
echo    - 10 produits/services
echo.
echo ⏹️  Pour arrêter: Ctrl+C
echo ========================================
echo.

call mvn spring-boot:run

pause
