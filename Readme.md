# Indice
- [Uso](#uso)
- [Post](#postear-en-compra)
- [Dependencias Comentadas](#dependencias-comentadas)
- [Rest template](#resttemplate)

## Uso
Primero debemos de autenticarnos con el microservicio Auth, cuando tengamos nuestro token, tendremos acceso a los [Endpoints](#endpoints) usando el BearerToken

## Endpoints
http://localhost:8026/compras para get general y post, en caso de querer obtener una compra por id, put o delete nuestro endpoint es http://localhost:8026/compras/{idProducto}/{idCliente}.
Consultar Swagger/OpenApi http://localhost:8026/swagger-ui.html.

## Postear en Compra
La url es http://localhost:8026/compras en método post

{

"idProducto": 1,

"idCliente": 2,

"cantidad": 5,

"fecha": "2024-10-24"

}

## Dependencias comentadas

Todo lo relacionado a Spring Security fue comentado para no tener inconvenientes en cuanto a la autenticación.

## RestTemplate
 Se encuentra en la clase AppConfig dentro del paquete config, está definido, nos vá a servir para consultar los demas microservicios.