# Salud Migrante - Indexer

## Introduccion

- Este proyecto realiza dos funciones:
	* Indexa los Copis generados desde Feeder
	* Expone un API para realizar busquedas

## Back End

- Proceso Batch que se encarga de indexar los Copis que se van generando (learn, experience, change). Los Copis se indexan en la plataforma ElasticSearch.
- API REST para la busqueda de Copis. Se exponen servicios, que por detras realizan busquedas en ElasticSearch.

## Front End

- No hay Front End

## Tecnologias

- Oracle Java 1.7: La aplicacion de servidor esta desarrollada en Java. 
- Librerias Java (gestionadas mediante Maven): 
	* Spring MVC
	* Spring Data
	* Jackson (JSON)
	* Apache Commons
	* ElasticSearch
	* otras
- Apache Tomcat 7: Servidor de aplicaciones
- MongoDB: como Base de Datos

## Dependencias

- Java 1.7 [Descargar](http://www.oracle.com/technetwork/es/java/javase/downloads/jdk7-downloads-1880260.html) 
- Maven 3  [Descargar](http://maven.apache.org/download.cgi)
- Apache Tomcat 7 [Descargar](http://tomcat.apache.org/download-70.cgi)
- MongoDB 2.6 [Descargar](http://www.mongodb.org/downloads#previous)
- ElasticSearch 1.4.4 [Descargar](https://www.elastic.co/downloads/elasticsearch)

## Instalación

- Para empaquetar el proyecto, es necesario colocarse en la carpeta del proyecto y ejecutar en el terminal

```
> mvn clean compile package
```
- Una vez empaquetado, en la carpeta **target** esta el fichero **indexer.war** que se puede instalar en el servidor de aplicaciones Tomcat.

 
## API

### Busqueda de Copis de Experience

```
http://host:8080/indexer/api/copis/experience?from=&size=&query=
```

Acepta los siguientes parametros de URL (opcionales):
* from (integer): indica desde que registro se regresan resultados. 0 por defecto, indicando que se regresa el primer registro.
* size (integer): indica el tamaño de pagina, el numero de registros a regresar. 10 por defecto.
* query (string): cadena de texto para realizar busquedas
* state (string): cadena de texto, dos caracteres, con los codigos ISO de los estados. Por defecto es "US".

### Busqueda de Copis de Learn

```
http://host:8080/indexer/api/copis/experience/{id}/learn?from=&size=
```

Acepta los siguientes parametros de URL (obligatorios):
* id: identificador del Copi Experience 

Acepta los siguientes parametros de URL (opcionales):
* from (integer): indica desde que registro se regresan resultados. 0 por defecto, indicando que se regresa el primer registro.
* size (integer): indica el tamaño de pagina, el numero de registros a regresar. 10 por defecto.

### Busqueda de Copis de Change

```
http://host:8080/indexer/api/copis/experience/{id}/change?from=&size=
```

Acepta los siguientes parametros de URL (obligatorios):
* id: identificador del Copi Experience 

Acepta los siguientes parametros de URL (opcionales):
* from (integer): indica desde que registro se regresan resultados. 0 por defecto, indicando que se regresa el primer registro.
* size (integer): indica el tamaño de pagina, el numero de registros a regresar. 10 por defecto.

