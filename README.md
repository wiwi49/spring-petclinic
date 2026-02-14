SPRING PETCLINIC – EXTENSION ORDER

TP Architecture logicielle
Architecture hexagonale (Ports & Adapters)

============================================================

1. PRÉSENTATION GÉNÉRALE

Ce projet est une extension du projet Spring Petclinic.
L’objectif est d’implémenter un nouveau cas métier : la gestion
du cycle de vie d’une commande (Order), en respectant strictement
les principes de l’architecture hexagonale (Ports & Adapters).

Ce travail a pour but pédagogique de démontrer :

- une séparation claire des responsabilités
- un domaine métier indépendant des frameworks
- une application testable et maintenable
- une gestion propre des bases de données via des profils Spring

============================================================

2. LANCER LE PROJET

PRÉREQUIS

- Java 17
- Git
- Gradle Wrapper (fourni avec le projet)

LANCEMENT AVEC LA BASE PAR DÉFAUT (H2)

Commande :

./gradlew bootRun

Par défaut, l’application démarre avec :

- une base H2 en mémoire
- une initialisation automatique du schéma et des données
- aucun prérequis de base de données externe

L’application est accessible à l’adresse :

http://localhost:8080

============================================================

3. LANCER LES TESTS

EXÉCUTER L’ENSEMBLE DES TESTS

./gradlew test

BUILD COMPLET AVEC VÉRIFICATIONS

./gradlew clean build

Les rapports de tests sont générés dans :

build/reports/tests/test/index.html

Les tests couvrent :

- le domaine métier (Order)
- les services applicatifs
- le flow complet de gestion d’une commande via des tests d’intégration

============================================================

4. ARCHITECTURE DU PROJET

Le projet suit une architecture hexagonale visant à découpler :

- le cœur métier
- les cas d’usage
- les interfaces techniques (REST, JPA)

Organisation des packages :

domain/
 └── order/
     ├── Order
     ├── OrderStatus
     ├── OrderStatusTransitions
     └── Exceptions métier

application/
 └── order/
     ├── port/in
     │    └── Use cases
     ├── port/out
     │    └── Ports de persistance
     └── service
          └── Implémentations des cas d’usage

adapter/
 ├── in
 │    └── REST Controller, DTO, Mapper
 └── out
      └── JPA, Repository, Mapper

------------------------------------------------------------

RÔLE DES COUCHES

DOMAIN
Contient les règles métier pures.
Aucune dépendance à Spring ou à la persistance.

APPLICATION
Implémente les cas d’usage via des ports.
Orchestre le domaine sans dépendre des frameworks.

ADAPTERS IN
Exposent l’application via une API REST.

ADAPTERS OUT
Implémentent la persistance (JPA).

============================================================

5. FLOW MÉTIER : ORDER

CRÉATION D’UNE COMMANDE

- Appel via l’API REST
- Délégation au CreateOrderUseCase
- Création d’un Order avec le statut initial CREATED
- Persistance via un port de sortie

MISE À JOUR DU STATUT

- Appel via l’API REST
- Délégation au UpdateOrderStatusUseCase
- Vérification des transitions autorisées
- Rejet des transitions invalides via une exception métier

GESTION DES TRANSITIONS

Les règles de transition sont centralisées dans :

OrderStatusTransitions

Toute transition invalide déclenche :

InvalidOrderStatusTransitionException

Aucune règle métier n’est implémentée dans les contrôleurs.

============================================================

6. PROFILS BASE DE DONNÉES

PROFIL PAR DÉFAUT : H2

- Base en mémoire
- Utilisée pour le développement et les tests
- Initialisation automatique via scripts SQL

db/h2/schema.sql
db/h2/data.sql

PROFIL POSTGRESQL

Activation :

./gradlew bootRun --args="--spring.profiles.active=postgres"

Configuration dans :

application-postgres.properties

Le changement de base de données ne nécessite aucune modification
du code métier.

============================================================

7. TESTS

Le projet inclut :

- des tests unitaires du domaine Order
- des tests des services applicatifs
- un test d’intégration principal :

OrderFlowIntegrationTest

Ce test valide le cycle complet d’une commande
de la création à la livraison.

============================================================

8. OBJECTIFS PÉDAGOGIQUES ATTEINTS

- Implémentation complète d’une architecture hexagonale
- Cas métier réaliste et non trivial
- Séparation stricte du domaine et des frameworks
- Tests unitaires et d’intégration fonctionnels
- Utilisation de profils pour la gestion des bases de données
