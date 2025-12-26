@echo off
setlocal

set TAILWIND_VERSION=v3.4.1
set TAILWIND_EXE=tailwindcss-windows-x64.exe
set TAILWIND_URL=https://github.com/tailwindlabs/tailwindcss/releases/download/%TAILWIND_VERSION%/%TAILWIND_EXE%

if not exist "%TAILWIND_EXE%" (
    echo Downloading Tailwind CSS standalone CLI...
    powershell -Command "Invoke-WebRequest -Uri '%TAILWIND_URL%' -OutFile '%TAILWIND_EXE%'"
)

echo Starting Tailwind CSS Watcher...
"%TAILWIND_EXE%" -i src/main/resources/static/css/input.css -o src/main/resources/static/css/application.css --watch
