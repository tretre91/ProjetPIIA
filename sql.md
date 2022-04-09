# Structure de la base de données

- User(idu, name, password, status, avatar)
- Video(idv, idc, name, path)
- Category(idc, name, status)
- Authorization(idu, idv)

Un utilisateur a un identifiant, un nom, un mot de passe, un statut (entier) et un avatar (chemin vers un fichier image).

Une vidéo a un identifiant, une catégorie, un titre, et un fichier associé.

Une catégorie a un identifiant, un nom et un niveau de privilège.

On associe également un utilisateur aux vidéos auxquelles il a accès

## Précisions et restrictions

Les identifiants (entiers) sont uniques, deux utilisateurs ne peuvent pas avoir le même nom, mais peuvent avoir le même mot de passe. Le statut est un entier entre 0 et 3 indiquant les privilèges de l'utilisateur :
- 0 -> admin
- 1 -> parent
- 2 -> adolescent
- 3 -> enfant

L'avatar s'il n'est pas renseigné est remplacé par une image par défaut. Les mots de passes sont (pour l'instant) stockés en clair et ne peuvent pas être vides, sauf pour les utilisateurs enfants.

Une vidéo est forcement associée à un fichier. Les noms de catégories sont uniques. Une vidéo peut être associée à 0 ou plusieurs catégories.

Une vidéo est visible par tous les utilisateurs de statut <= 1 (admin et parent), et peut être visible à certains utiliisateurs adolescents et aux utilisateurs enfants

L'admin et les parents ont accès à toutes les vidéos de la bibliotèqhue, les adolescents et les enfants n'ont accès qu'aux vidéos autorisées par leur parents.

## Tables

```sql
-- on utilise sqlite ici

CREATE TABLE IF NOT EXISTS user (
    idu INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    status INTEGER NOT NULL CHECK (0 <= status AND status <= 3)
    avatar TEXT,
    CHECK (password NOT LIKE '' OR status = 3));

CREATE TABLE IF NOT EXISTS video (
    idv INTEGER PRIMARY KEY AUTOINCREMENT,
    idc INTEGER REFERENCES category(idc) NOT NULL,
    name TEXT UNIQUE NOT NULL,
    path TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS category (
    idc INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    status INTEGER NOT NULL CHECK (0 <= status AND status <= 3));

CREATE TABLE IF NOT EXISTS authorization (
    idu INTEGER REFERENCES user(idu) ON DELETE CASCADE NOT NULL,
    idv INTEGER REFERENCES video(idv) ON DELETE CASCADE NOT NULL,
    UNIQUE (idu, idv));
```

## Accès aux données

Le compte admin peut modifier le mot de passe de n'importe quelle autre compte (y compris celui du compte admin).

On considère dans la suite les parents comme des utilisateurs administrateurs.

Un administrateur peut créer un compte, rendre des vidéos visibles aux utilisateurs adolescents ou enfants, créer des catégories et ranger les vidéos dans des catégories.

