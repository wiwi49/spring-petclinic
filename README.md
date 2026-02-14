SPRING PETCLINIC – EXTENSION ORDER

TP Architecture logicielle
Architecture hexagonale (Ports & Adapters)

============================================================

1. PRÉSENTATION GÉNÉRALE

Ce projet est une extension du projet Spring Petclinic.
L’objectif est d’implémenter un nouveau cas métier : la gestion
du cycle de vie d’une commande (Order), en respectant strictement
les principes de l’architecture hexagonale (Ports & Adapters).

Ce travail a un objectif pédagogique clair : transformer une
application Spring Boot classique en une application mieux
structurée, plus maintenable et plus testable, en isolant
le domaine métier des aspects techniques.

Les objectifs principaux sont :

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

- le domaine métier (Order, transitions de statut)
- les services applicatifs (cas d’usage)
- le flow complet de gestion d’une commande via des tests d’intégration

============================================================

4. ARCHITECTURE DU PROJET

Le projet suit une architecture hexagonale visant à découpler :

- le cœur métier
- les cas d’usage
- les interfaces techniques (REST, JPA)

Le domaine métier est placé au centre de l’application et ne dépend
d’aucune technologie spécifique.

------------------------------------------------------------

ORGANISATION DES PACKAGES

domain/
 └── order/
     ├── Order
     ├── OrderStatus
     ├── OrderStatusTransitions
     └── Exceptions métier

application/
 └── order/
     ├── port/in
     │    └── Use cases (interfaces)
     ├── port/out
     │    └── Ports de persistance
     └── service
          └── Implémentations des cas d’usage

adapter/
 ├── in
 │    └── REST Controller, DTO, Mapper, gestion des erreurs
 └── out
      └── JPA, Repository Spring Data, Mapper de persistance

------------------------------------------------------------

RÔLE DES COUCHES

DOMAIN  
Contient les règles métier pures.
Aucune dépendance à Spring, à JPA ou à la base de données.

APPLICATION  
Implémente les cas d’usage via des ports.
Orchestre le domaine sans dépendre des détails techniques.

ADAPTERS IN  
Exposent l’application vers l’extérieur (API REST).
Ils dépendent uniquement des ports d’entrée.

ADAPTERS OUT  
Implémentent les ports de sortie (persistance).
Ce sont les seuls composants autorisés à accéder à la base de données.

============================================================

5. FLOW MÉTIER : ORDER

CRÉATION D’UNE COMMANDE

- Appel via l’API REST
- Délégation au CreateOrderUseCase
- Création d’un objet Order avec le statut initial CREATED
- Persistance via un port de sortie

MISE À JOUR DU STATUT

- Appel via l’API REST
- Délégation au UpdateOrderStatusUseCase
- Vérification des transitions autorisées
- Rejet des transitions invalides via une exception métier

GESTION DES TRANSITIONS

Les règles de transition sont centralisées dans :

OrderStatusTransitions

Transitions autorisées :

CREATED → PAID → SHIPPED → DELIVERED

Toute transition invalide déclenche :

InvalidOrderStatusTransitionException

Aucune règle métier n’est implémentée dans les contrôleurs REST.

============================================================

6. PROFILS BASE DE DONNÉES

PROFIL PAR DÉFAUT : H2

- Base de données en mémoire
- Utilisée pour le développement et les tests
- Initialisation automatique via scripts SQL

db/h2/schema.sql  
db/h2/data.sql

PROFIL POSTGRESQL

Activation :

./gradlew bootRun --args="--spring.profiles.active=postgres"

Configuration située dans :

application-postgres.properties

Le changement de base de données ne nécessite aucune modification
du code métier, ce qui valide l’indépendance du domaine.

============================================================

7. TESTS

Le projet inclut :

- des tests unitaires du domaine Order
- des tests des services applicatifs
- un test d’intégration principal : OrderFlowIntegrationTest

Ce test valide le cycle complet d’une commande :

CREATED → PAID → SHIPPED → DELIVERED

Les tests garantissent :

- la cohérence des règles métier
- la validité des transitions de statut
- la robustesse de l’architecture mise en place

============================================================

8. TEST DES ENDPOINTS REST

CRÉATION D’UNE COMMANDE

POST /api/orders

Exemple avec curl :

curl -X POST http://localhost:8080/api/orders

Réponse attendue :

{
  "id": 1,
  "status": "CREATED"
}

------------------------------------------------------------

MISE À JOUR DU STATUT D’UNE COMMANDE

PUT /api/orders/{id}/status

Exemple :

curl -X PUT http://localhost:8080/api/orders/1/status \
     -H "Content-Type: application/json" \
     -d '{"status":"PAID"}'

Réponse attendue :

{
  "id": 1,
  "status": "PAID"
}

Toute transition invalide retourne une erreur HTTP 400,
gérée via une exception métier.

============================================================

9. OBJECTIFS PÉDAGOGIQUES ATTEINTS

- Implémentation complète d’une architecture hexagonale
- Cas métier réaliste et non trivial
- Séparation stricte du domaine et des frameworks
- Cas d’usage explicites via des ports
- Adaptateurs clairement identifiés
- Tests unitaires et d’intégration fonctionnels
- Gestion des bases de données par profils Spring

============================================================
