# Projet PIIA

## Build

Les bibliothèques nécessaires pour exécuter le projet sont [JavaFX](https://openjfx.io/) et [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc).

## Structure du code

Le code est divisé en 4 packages :

### view

Le package view contient les classes relatives à la partie visible de l'application.

- `Page` : Enumeration définissant les différentes pages de l'aplication ainsi que leur fichier fxml associé.
- `View` : Classe implémentant les fonctions permettant de changer de page.

### controller

Le package controller contient le code qui gère la vue et qui fait le lien entre la vue et le modèle. Un fichier de la forme \<Something\>Controller.java est le contrôleur propre au fichier \<Something\>.fxml.

- `State` : Classe abstraite contenant des informations sur l'état global de l'application.
- `LoginController` : Contrôleur de la page de connexion (login.fxml).
- `UsersController` : Contrôleur de la page de choix de l'utilisateur (users.fxml).
- `UserCardController` : Contrôleur des cartes d'utilisateur de la page de choix de l'utilisateur (userCard.fxml).

### model

Le package model comprend les classes s'occupant de la représentation des données de l'application.

- `Database` : Classe abstraite qui gère les accès à la base de données.
- `Status` : Enumeration représentant les différents types d'utilisateurs.
- `User` : Classe représentant un utilisateur.
- `Users` : Classe abstraite contenant des fonctions permettant de récupèrer des informations sur les utilisateurs depuis la base de données.

### resources

Package contenant toutes les ressources de l'application :

- fxml : dossier contenant les fichiers fxml utilisés par l'application
- css : dossier contenant les feuilles de style utilisées par les fichiers fxml
- icons : dossier contenant les icônes utilisées par l'application
- database.db : le fichier de base de donnée de l'application
