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

## Ajouter des pages

Pour ajouter la page `exemple` :

1. Concevoir la page en question dans SceneBuilder
2. L'enregistrer dans le fichier `exemple.fxml`
3. Placer le fichier dans le dossier `resources/fxml`
4. Créer une nouveau fichier `ExempleController.java`
    ```java
    package controller;

    import java.net.URL;
    import java.util.ResourceBundle;

    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;

    public class ExempleController implements Initializable {
        @Override
        public void initialize(URL url, ResourceBundle resources) {
            // ici le code à executer pour initialiser la page
        }
    }
    ```
5. Ouvrir `exemple.fxml` avec un éditeur de texte, rajouter `fx:controller="controller.ExempleController"` à la fin de la balise du conteneur parent (voir [login.fxml ligne 12](./src/resources/fxml/login.fxml) par exemple)
6. Dans [l'enum `Page`](./src/view/Page.java) du package `view`, rajouter une valeur `EXEMPLE("exemple.fxml")`

Supposons que l'on veuille créer une page vide avec un bouton retour qui ramène vers la page `LOGIN`

- on va créer un nouveau projet vide dans SceneBuilder
- dans la barre de gauche, rubrique "Container", choisir un layout qui sera notre base (par exemple un Pane). Pour placer un élément il suffit de faire un glisser-déposer depuis le nom de l'élément dans la barre de droite vers l'espace central.
- ensuite, dans la rubrique "Controls", choisir un Button et le placer dans le Pane.
- sauvegarder le fichier
- continuer à partir de l'étape 1 plus haut

A partir de là on a deux façons d'ajouter une action lorsque l'on cique sur le bouton :

### 1ère méthode : indiquer dans SceneBuilder quelle fonction appeler quand on clique

Après avoir placé le bouton dans SceneBuilder, dans la barre de droite, section "code", on peut spécifier un nom de méthode dans le champ "onAction" (on peut taper onAction dans la barre de recherche), on va appeler cette méthode `goBack`.

Lorsque le bouton sera cliqué, le programme cherchera une méthode `void goback()` dans la classe `ExempleController`, pour que cette méthode puisse être lue depuis le fichier fxml il faut lui donner l'annotation `@FXML` (dans ce cas elle n'a même plus besoin d'être publique), on peut ensuite mettre dans le corps de la méthode le code à exécuter en cas de clic.

Dans la classe `ExempleController` on peut définir cette fonction comme suit :
```java
...

import view.View;
import view.Page;

public class ExempleController implements Initializable {
    ...

    @FXML
    private void goBack() {
        View.switchPage(Page.LOGIN);
    }

    ...
}
```

### 2ème méthode : accèder au bouton depuis la classe ExempleController

Une autre façon de faire est d'acceder au widget directement depuis le contrôleur, comme si on l'avait crée dans le programme java.

Après avoir placé le bouton dans SceneBuilder, dans la barre de droite, section "code", on peut donner un identifiant  (champ "fx:id") à notre bouton, appelons le `boutonRetour`.

Dans la classe `ExempleController` on peut déclarer une variable de type `Button` avec **exactement** le même nom que celui donné dans SceneBuilder, et l'annotation `@FXML` discutée plus haut. On pourra ensuite manipuler cette variable comme si on l'avait crée directement dans notre programme.

Ça donne :
```java
...
import javafx.scene.control.Button;

import view.View;
import view.Page;

public class ExempleController implements Initializable {
    @FXML
    private Button boutonRetour;
    ...

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // à partir de ce point, toutes les variables importées depuis SceneBuilder sont correctement initialisées
        boutonRetour.setOnAction(event -> { View.switchPage(Page.LOGIN); });       
    }

    ...
}
```
#### Conclusion

Les deux méthodes sont équivalentes pour le cas précedent, mais chacune présente des avantages dans certaines situations :

Utiliser la méthode 1 ("tout" faire dans SceneBuilder) pour des éléments statiques qui ne changeront pas d'état durant l'exécution
- avantage : génère moins de code, permet d'avoir des classes un peu plus legères en terme de contenu
- exemple : la majorité des boutons

Utiliser la méthode 2 pour des éléments dynamiques dont l'état peut changer
  - avantage : permet de contrôler plus finement le comportement de certains widget
  - exemple  : l'input dans la page de login qui devient rouge si le mot de passe est éronné
