# Indice
- [Post](Postear-en-Compra)
- [Dependencias Comentadas](Dependencia-comentadas)
- [Rest template](RestTemplate)

## Postear en Compra
La url es http://localhost:8026/ en método post

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