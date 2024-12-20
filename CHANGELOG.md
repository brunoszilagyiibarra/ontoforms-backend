# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
### Changed
### Deprecated
### Removed
### Fixed
### Security

## [v4.1] - 2024-11-12
### Fixed
- Serialización sobre clase Form para los distintos tipos de campos. 

## [v4.0] - 2024-11-11
### Removed
- Se remueven todas las customizaciones del formulario para el caso de BreastScreening quedando el algoritmo genérico.

## [v3.0] - 2024-11-11
### Added
- Se agrega soporte en el FusekiTripleStoreClient para los datasets de individuos y configuraciones. Guardar y Obtener.
- Parametrizo propiedades de autenticación básica contra triplestore.
- Se agrega configuración de aplicación - ontology
### Changed
- Se actualizan parámetros de Cors
- Se modifican configuraciones de 'artifice-class', 'translations', 'calculated-property' para utilizar el repositorio de configuraciones y 
  todo el ABM contra el triplestore.


## [v2.0] - 2024-11-10
### Changed
- Se unifican todos los controllers en OnformsController para simplificar la API.
- Se renombra el paquete root para cambiar a "ontoforms"


## [v1.3] - 2024-09-08
### Added
- Servicio para poder modificar el uso de stub o uso real de calculadora IBIS. 
