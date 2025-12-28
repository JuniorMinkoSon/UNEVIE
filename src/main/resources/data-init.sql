-- =====================================================
-- UNEVIE - Script d'initialisation de la base de données
-- =====================================================
-- Ce script crée et remplit les tables avec des données de démonstration
-- Pour PostgreSQL

-- =====================================================
-- 1. QUARTIERS
-- =====================================================

INSERT INTO
    quartiers (
        nom,
        commune,
        latitude,
        longitude,
        rayon_couverture,
        created_at,
        updated_at
    )
VALUES (
        'Cocody',
        'Abidjan',
        5.3599,
        -3.9866,
        5.0,
        NOW(),
        NOW()
    ),
    (
        'Yopougon',
        'Abidjan',
        5.3433,
        -4.0587,
        5.0,
        NOW(),
        NOW()
    ),
    (
        'Plateau',
        'Abidjan',
        5.3267,
        -4.0122,
        3.0,
        NOW(),
        NOW()
    ),
    (
        'Marcory',
        'Abidjan',
        5.2964,
        -3.9777,
        4.0,
        NOW(),
        NOW()
    ),
    (
        'Treichville',
        'Abidjan',
        5.2928,
        -4.0059,
        3.5,
        NOW(),
        NOW()
    );

-- =====================================================
-- 2. ENTREPRISES PARTENAIRES
-- =====================================================

-- Babi Location (Véhicules - Cocody)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Babi Location',
        'Location de véhicules haut de gamme avec chauffeur disponible. Flotte moderne et bien entretenue pour tous vos déplacements professionnels et personnels.',
        null,
        '+225 07 00 00 01',
        'contact@babilocation.ci',
        'Riviera Golf, Cocody',
        'VEHICULE',
        1, -- Cocody
        5.3599,
        -3.9866,
        true,
        '2024-01-01',
        '2026-12-31',
        'Partenaire Gold - Services premium',
        10.0,
        4.8,
        127,
        NOW(),
        NOW(),
        NOW()
    );

-- Sponguy Location (Véhicules - Yopougon)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Sponguy Location',
        'Spécialiste de la location de véhicules pour tous vos déplacements. Service 24/7, assistance routière incluse.',
        null,
        '+225 07 00 00 02',
        'info@sponguylocation.ci',
        'Siporex, Yopougon',
        'VEHICULE',
        2, -- Yopougon
        5.3433,
        -4.0587,
        true,
        '2024-03-01',
        '2026-02-28',
        'Partenaire standard',
        10.0,
        4.5,
        89,
        NOW(),
        NOW(),
        NOW()
    );

-- Résidences Premium CI (Résidences - Plateau)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Résidences Premium CI',
        'Locations de résidences meublées standing pour séjours courts et longs. Confort garanti, sécurité 24/7.',
        null,
        '+225 07 00 00 03',
        'contact@residencespremium.ci',
        'Rue des Jardins, Plateau',
        'RESIDENCE',
        3, -- Plateau
        5.3267,
        -4.0122,
        true,
        '2024-02-01',
        '2025-01-31',
        'Renouvellement en cours',
        10.0,
        4.9,
        203,
        NOW(),
        NOW(),
        NOW()
    );

-- Assinie Beach Resort (Résidences - Cocody)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Assinie Beach Resort',
        'Villas et appartements en bord de mer à Assinie. Cadre idyllique pour vos vacances, week-ends et séminaires.',
        null,
        '+225 07 00 00 04',
        'booking@assinieresort.ci',
        'Route d''Assinie, Cocody',
        'RESIDENCE',
        1, -- Cocody
        5.3599,
        -3.9866,
        true,
        '2024-06-01',
        '2026-05-31',
        'Partenaire Gold',
        10.0,
        5.0,
        156,
        NOW(),
        NOW(),
        NOW()
    );

-- Délices d'Afrique (Traiteur - Marcory)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Délices d''Afrique',
        'Traiteur spécialisé dans la cuisine africaine et internationale. Pour tous vos événements : mariages, baptêmes, séminaires.',
        null,
        '+225 07 00 00 05',
        'commandes@delicesdafrique.ci',
        'Zone 4, Marcory',
        'TRAITEUR',
        4, -- Marcory
        5.2964,
        -3.9777,
        true,
        '2023-10-01',
        '2025-09-30',
        'Partenaire historique',
        10.0,
        4.7,
        312,
        NOW(),
        NOW(),
        NOW()
    );

-- Royal Catering Services (Traiteur - Treichville)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'Royal Catering Services',
        'Service traiteur haut de gamme pour mariages, séminaires et événements corporate. Équipe de chefs professionnels.',
        null,
        '+225 07 00 00 06',
        'events@royalcatering.ci',
        'Boulevard VGE, Treichville',
        'TRAITEUR',
        5, -- Treichville
        5.2928,
        -4.0059,
        true,
        '2024-01-15',
        '2026-01-14',
        'Service premium',
        10.0,
        4.8,
        178,
        NOW(),
        NOW(),
        NOW()
    );

-- EventMakers Pro (Événements - Cocody)
INSERT INTO
    entreprises (
        nom,
        description,
        logo,
        telephone,
        email,
        adresse,
        categorie,
        quartier_id,
        latitude,
        longitude,
        contrat_actif,
        date_debut_contrat,
        date_fin_contrat,
        notes_contrat,
        commission_pourcentage,
        note_globale,
        nombre_avis,
        date_creation,
        created_at,
        updated_at
    )
VALUES (
        'EventMakers Pro',
        'Organisation complète d''événements : mariages, anniversaires, séminaires. Équipe professionnelle, matériel moderne.',
        null,
        '+225 07 00 00 07',
        'hello@eventmakers.ci',
        'Les Deux Plateaux, Cocody',
        'EVENEMENT',
        1, -- Cocody
        5.3599,
        -3.9866,
        true,
        '2024-04-01',
        '2027-03-31',
        'Partenaire stratégique',
        10.0,
        4.9,
        245,
        NOW(),
        NOW(),
        NOW()
    );

-- =====================================================
-- 3. PRODUITS / SERVICES (Exemples)
-- =====================================================

-- Produits Babi Location
INSERT INTO
    produit (
        nom,
        categorie,
        prix,
        description,
        image_url,
        disponible,
        entreprise_id,
        quartier_id,
        requires_permis,
        created_at,
        updated_at
    )
VALUES (
        'Toyota Camry 2023',
        'Berline',
        35000,
        'Berline confortable, climatisée, GPS intégré',
        null,
        true,
        1,
        1,
        true,
        NOW(),
        NOW()
    ),
    (
        'Mercedes Classe E',
        'Berline Premium',
        75000,
        'Luxe et confort pour vos déplacements VIP',
        null,
        true,
        1,
        1,
        true,
        NOW(),
        NOW()
    ),
    (
        'Toyota Prado',
        'SUV',
        60000,
        '4x4 spacieux, idéal pour les familles',
        null,
        true,
        1,
        1,
        true,
        NOW(),
        NOW()
    );

-- Produits Sponguy Location
INSERT INTO
    produit (
        nom,
        categorie,
        prix,
        description,
        image_url,
        disponible,
        entreprise_id,
        quartier_id,
        requires_permis,
        created_at,
        updated_at
    )
VALUES (
        'Renault Duster',
        'SUV',
        30000,
        'SUV économique et robuste',
        null,
        true,
        2,
        2,
        true,
        NOW(),
        NOW()
    ),
    (
        'Peugeot 508',
        'Berline',
        40000,
        'Élégance et performance',
        null,
        true,
        2,
        2,
        true,
        NOW(),
        NOW()
    );

-- Produits Résidences Premium CI
INSERT INTO
    produit (
        nom,
        categorie,
        prix,
        description,
        image_url,
        disponible,
        entreprise_id,
        quartier_id,
        requires_permis,
        created_at,
        updated_at
    )
VALUES (
        'Studio Meublé',
        'Logement',
        150000,
        'Studio moderne, tout équipé, WiFi inclus',
        null,
        true,
        3,
        3,
        false,
        NOW(),
        NOW()
    ),
    (
        'Appartement 2 pièces',
        'Logement',
        250000,
        'F2 spacieux, balcon, parking sécurisé',
        null,
        true,
        3,
        3,
        false,
        NOW(),
        NOW()
    ),
    (
        'Villa 4 pièces',
        'Logement',
        500000,
        'Villa standing, piscine, jardin',
        null,
        true,
        3,
        3,
        false,
        NOW(),
        NOW()
    );

-- Produits Assinie Beach Resort
INSERT INTO
    produit (
        nom,
        categorie,
        prix,
        description,
        image_url,
        disponible,
        entreprise_id,
        quartier_id,
        requires_permis,
        created_at,
        updated_at
    )
VALUES (
        'Bungalow Vue Mer',
        'Logement',
        180000,
        'Bungalow pieds dans l''eau, 2 chambres',
        null,
        true,
        4,
        1,
        false,
        NOW(),
        NOW()
    ),
    (
        'Villa Luxe Assinie',
        'Logement',
        400000,
        'Villa 5 étoiles, piscine privée, plage privée',
        null,
        true,
        4,
        1,
        false,
        NOW(),
        NOW()
    );

-- =====================================================
-- RÉSUMÉ
-- =====================================================
-- ✅ 5 Quartiers créés
-- ✅ 7 Entreprises créées (2 Véhicules, 2 Résidences, 2 Traiteurs, 1 Événement)
-- ✅ 10 Produits/Services exemples
-- ✅ Tous avec contrat EasyService actif

SELECT
    'Initialisation terminée !' as message,
    (
        SELECT COUNT(*)
        FROM quartiers
    ) as quartiers_count,
    (
        SELECT COUNT(*)
        FROM entreprises
    ) as entreprises_count,
    (
        SELECT COUNT(*)
        FROM produit
    ) as produits_count;