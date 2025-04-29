SpringBootWebfluxApiVentasApplication

microservicio ventas ejercicio practico, con mongodb:
==================================================
agregar endpoint de crear venta
-	una venta debe tener minimo 1 producto 									                -> OK
-	los productos a comprar en la misma venta no pueden ser mayor a la 
    cantidad de stock que existe del producto 								            -> OK
-	al crear la venta se debe registrar la fecha y hora de cuando se creo   -> OK
-	se debe modificar el producto para que tenga la propiedad del stock     -> OK
-	al realizar la venta de debe restar el stock de los productos vendidos  -> OK
agreggar endpoint de listar ventas
-	las ventas deben traer el listado de produtos vendidos                  -> OK
    http://localhost:8085/api/ventas?size=10&page=0

se adjunta coleccion de postman: spring-boot-webflux-apiventas.postman_collection.json

endpoint en curl:

listar-productos: curl --location 'http://localhost:8085/api/productos'

crear-producto: curl --location 'http://localhost:8085/api/productos' \
--header 'Content-Type: application/json' \
--data '{
    "codigo": "IP01",
    "nombre": "Iphone 13",
    "precio": 1000,
    "stock":10,
    "categoria": {
        "id": "68101a4fb42022760ec30a77",
        "nombre": "Electronico"
    }
}'

vender-productos: curl --location 'http://localhost:8085/api/ventas' \
--header 'Content-Type: application/json' \
--data '{
  "total": 4500,
  "productosCodStock": ["TV01:2","IP01:2"]
}'

listar-ventas: curl --location 'http://localhost:8085/api/ventas?size=10&page=0' \
--data ''



