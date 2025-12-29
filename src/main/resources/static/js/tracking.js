/**
 * Logique de tracking en temps réel pour UNEVIE
 */

// Configuration de la carte
const mapConfig = {
    startLat: 5.3400, // Centre Abidjan
    startLng: -4.0100,
    zoom: 12
};

let map;
let vehicleMarkers = {}; // Stocke les marqueurs par ID véhicule
let stompClient;

// Icônes personnalisées
const carIcon = (color) => L.divIcon({
    className: 'custom-div-icon',
    html: `<div style="background-color: ${color}; width: 30px; height: 30px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 5px rgba(0,0,0,0.3); display: flex; justify-content: center; align-items: center;">
            <i class="fas fa-car text-white" style="font-size: 14px;"></i>
           </div>`,
    iconSize: [30, 30],
    iconAnchor: [15, 15],
    popupAnchor: [0, -15]
});

// Couleurs par statut
const statusColors = {
    'DISPONIBLE': '#10B981', // Vert
    'EN_COURSE': '#3B82F6',  // Bleu
    'HORS_SERVICE': '#EF4444', // Rouge
    'EN_RETOUR': '#F59E0B'   // Orange
};

document.addEventListener('DOMContentLoaded', () => {
    initMap();
    connectWebSocket();
    loadInitialData();
});

function initMap() {
    // Initialiser Leaflet
    map = L.map('map').setView([mapConfig.startLat, mapConfig.startLng], mapConfig.zoom);

    // Ajouter layer Satellite (Esri World Imagery)
    L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
        attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
    }).addTo(map);

    // Ajouter layer noms de rues par dessus (transparent)
    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 20
    }).addTo(map);
}

function connectWebSocket() {
    const socket = new SockJS('/ws-tracking');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // Désactiver logs debug

    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        updateConnectionStatus(true);

        // S'abonner aux mises à jour de position
        stompClient.subscribe('/topic/vehiclePositions', (message) => {
            const position = JSON.parse(message.body);
            updateVehicleMarker(position);
            updateVehicleList(position);
        });

    }, (error) => {
        console.error('WebSocket error:', error);
        updateConnectionStatus(false);
        // Retry connection after 5 seconds
        setTimeout(connectWebSocket, 5000);
    });
}

function updateConnectionStatus(connected) {
    const indicator = document.getElementById('connection-status');
    if (connected) {
        indicator.classList.remove('bg-red-500');
        indicator.classList.add('bg-green-500');
        indicator.title = "Connecté en temps réel";
    } else {
        indicator.classList.remove('bg-green-500');
        indicator.classList.add('bg-red-500');
        indicator.title = "Déconnecté - Tentative de reconnexion...";
    }
}

async function loadInitialData() {
    try {
        const response = await fetch('/api/tracking/active');
        const positions = await response.json();

        // Initialiser la liste et les marqueurs
        document.getElementById('vehicle-list').innerHTML = '';
        positions.forEach(pos => {
            updateVehicleMarker(pos);
            addVehicleToList(pos);
        });

        updateStats(positions);

    } catch (error) {
        console.error("Erreur chargement données initiales:", error);
    }
}

function updateVehicleMarker(position) {
    const color = statusColors[position.statut] || '#9CA3AF';
    const latlng = [position.latitude, position.longitude];

    if (vehicleMarkers[position.vehiculeId]) {
        // Mettre à jour marqueur existant avec animation
        const marker = vehicleMarkers[position.vehiculeId];
        marker.setLatLng(latlng);
        marker.setIcon(carIcon(color)); // Mettre à jour couleur si statut change

        // Mettre à jour popup
        marker.getPopup().setContent(createPopupContent(position));

    } else {
        // Créer nouveau marqueur
        const marker = L.marker(latlng, {
            icon: carIcon(color),
            title: `Véhicule #${position.vehiculeId}`
        }).addTo(map);

        marker.bindPopup(createPopupContent(position));
        vehicleMarkers[position.vehiculeId] = marker;
    }
}

function createPopupContent(position) {
    return `
        <div class="p-2">
            <h3 class="font-bold text-gray-800">Véhicule #${position.vehiculeId}</h3>
            <div class="text-sm text-gray-600 mb-2">
                <span class="status-badge status-${position.statut}">${position.statut}</span>
            </div>
            <div class="text-xs text-gray-500">
                <i class="fas fa-tachometer-alt mr-1"></i> ${position.vitesse ? position.vitesse.toFixed(0) : 0} km/h
            </div>
            ${position.adresseApproximative ? `
            <div class="text-xs text-gray-500 mt-1">
                <i class="fas fa-map-marker-alt mr-1"></i> ${position.adresseApproximative}
            </div>` : ''}
            <div class="text-xs text-gray-400 mt-2">
                Màj: ${new Date(position.timestamp).toLocaleTimeString()}
            </div>
        </div>
    `;
}

function updateVehicleList(position) {
    // Vérifier si l'élément existe déjà dans la liste
    const existingItem = document.getElementById(`vehicle-item-${position.vehiculeId}`);

    if (existingItem) {
        // Mettre à jour
        existingItem.querySelector('.v-statut').className = `v-statut status-badge status-${position.statut} text-xs`;
        existingItem.querySelector('.v-statut').textContent = position.statut;
        existingItem.querySelector('.v-speed').textContent = `${position.vitesse ? position.vitesse.toFixed(0) : 0} km/h`;

        // Animation flash
        existingItem.classList.add('bg-blue-50');
        setTimeout(() => existingItem.classList.remove('bg-blue-50'), 500);

    } else {
        addVehicleToList(position);
    }

    // Mettre à jour les stats
    recalcStats();
}

function addVehicleToList(position) {
    const list = document.getElementById('vehicle-list');
    const color = statusColors[position.statut] || '#9CA3AF';

    const item = document.createElement('div');
    item.id = `vehicle-item-${position.vehiculeId}`;
    item.className = 'vehicle-card bg-white p-3 rounded-lg shadow-sm border border-gray-100 flex justify-between items-center';
    item.onclick = () => focusOnVehicle(position.vehiculeId);

    item.innerHTML = `
        <div class="flex items-center">
            <div class="w-8 h-8 rounded-full flex items-center justify-center mr-3" style="background-color: ${color}20; color: ${color}">
                <i class="fas fa-car"></i>
            </div>
            <div>
                <div class="font-bold text-sm text-gray-800">Véhicule #${position.vehiculeId}</div>
                <div class="text-xs text-gray-500">
                    <i class="fas fa-map-pin mr-1"></i>
                    <span class="v-location">${position.adresseApproximative || 'Abidjan'}</span>
                </div>
            </div>
        </div>
        <div class="text-right">
            <div class="v-statut status-badge status-${position.statut} text-xs mb-1">${position.statut}</div>
            <div class="v-speed text-xs text-gray-400 font-mono">${position.vitesse ? position.vitesse.toFixed(0) : 0} km/h</div>
        </div>
    `;

    list.prepend(item);
}

function focusOnVehicle(id) {
    if (vehicleMarkers[id]) {
        map.flyTo(vehicleMarkers[id].getLatLng(), 15);
        vehicleMarkers[id].openPopup();
    }
}

function updateStats(positions) {
    const total = positions.length;
    const active = positions.filter(p => p.statut === 'EN_COURSE').length;
    const available = positions.filter(p => p.statut === 'DISPONIBLE').length;

    document.getElementById('total-count').textContent = total;
    document.getElementById('active-count').textContent = active;
    document.getElementById('available-count').textContent = available;
}

function recalcStats() {
    // Recalcul simple basé sur ce qu'on a en mémoire (approximation pour l'instant)
    // Idéalement on referait un fetch ou on maintiendrait un state local complet
}
