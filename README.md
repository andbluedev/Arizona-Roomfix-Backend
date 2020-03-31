# RoomFix - API

Ce projet est une API Spring Boot avec Hibernate/JPA comme ORM. 
La base de données utilisée est une MySQL.

## Configurer les paramètres et le profil

La première chose à faire pour lancer l'application est de configurer les paramètres spécifiques à votre machine.
Tous les paramètres ont des valeurs par défaut faites pour faciliter la première prise en main.

| Usage            | Format               | Valeur par défaut      | Variable à modifier    |
|------------------|----------------------|------------------------|------------------------|
| BDD              | user:mdp@host/dbname | root@localhost/roomfix | DB_URL                 |


[Modifier facilement ce tableau sur TablesGenerator](https://www.tablesgenerator.com/markdown_tables)


## Le profil de développement

Nous utilisons [les profiles Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html)
pour configurer les variables spécifiques à chaque environnement (dev, production, etc.).

Pour lancer l'application en local, vous devez la lancer avec le profil `dev`.

* Avec IntelliJ : [suivre ce tuto sur Stacko](https://stackoverflow.com/a/39775038/7248759)
* Avec Maven et le plugin : `./mvnw spring-boot:run`

## Les plugins Intellij recommandés

- Lombok : pour les POJO (java beans) nous utilisons @data pour auto-générer les getters, setters et constructeur. Ce plugin à ajouter
à Intellij permet de faire comprendre à l'IDE que ces fonctions existent mais ne sont pas écrites explicitement.
(Evite que l'IDE dise qu'il ne trouve pas les fonctions)


## Les Endpoints

Toutes les endpoints sont sur la collection postman associée au repo.


## Docker

Docker ici est utilisé pour executer en production l'api.

Pour builder l'image docker.

```
docker build . -t roomfix-api:v1
```

Runner le conteneur sur le port 8080 de la machine (accessible sur le port 8080 de localhost par exemple).

```
docker run <image-roomfix-api> -p 8080:8000 -e DB_URL=<database-user>:<database-password>@<database-url>:3306/roomfix
```

Par exemple:

```
docker run roomfix-api:v1 -p 8080:8000 -e DB_URL=root:root@localhost:3306/roomfix
```

Si vous runnez MySQL avec Docker pensez bien à changer `localhost` par `<mysql-container-name>` afin que les containers peuvent communiquer (carte réseau MacOS et Windows ne permettent pas à Docker de communiquer entre eux sur le localhost).

Il est également possible de runner l'image avec le docker-compose qui contient une docker MySQL et de quoi run l'api (il faut la builder avec les consignes ci-dessus).

Pour run API + MySQL : 
```
docker-compose up -d
```

Pour un MySQL
```
docker-compose up mysqldb -d
```










