package org.compra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.compra.model.Compra;
import org.compra.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Compra", description = "Operaciones relacionadas con compras realizadas")
public class CompraController {
    @Autowired
    private CompraService compraService;
   /* @Autowired
    private JWT_UTILIDADES jWT_UTILIDADES;

    @GetMapping("/auth/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Lógica para validar el token
        String token = authorizationHeader.substring(7); // Si tiene el prefijo "Bearer "
        boolean isValid = JWT_UTILIDADES.validateToken(token); // Llama al método de validación

        if (isValid) {
            return ResponseEntity.ok("Token válido");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }
*/

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compras obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<List<Compra>> listar() {
        List<Compra> compras=compraService.getCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @Operation(summary = "Obtener las compras con la idProducto e idCliente especificas", description = "Devuelve una lista de todos las compras con las respectivas ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compras obtenida con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
            @ApiResponse(responseCode = "404", description = "No se encontraron esas compras")
    })
    @GetMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> getCompra(@PathVariable Long idProducto, @PathVariable Long idCompra) {
        Compra compra=compraService.findById(idProducto, idCompra);
        if(compra!=null){
            return new ResponseEntity<>(compra, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Crear una nueva compra", description = "Agrega una compra al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Compra> postCompra(@RequestBody Compra compra) {
        Compra compraGuardada = compraService.save(compra);
        if(compraGuardada != null) {
            return ResponseEntity.ok(compraGuardada);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Actualizar una compra existente", description = "Modifica los datos de una compra dada su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrado")
    })
    @PutMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> updateCompra(@PathVariable Long idCliente, @PathVariable Long idProducto, @PathVariable Long idCompra, @RequestBody Compra compra){
        Compra c=compraService.updateCompra(idCliente, idProducto, idCompra, compra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Eliminar una compra", description = "Elimina una compra del sistema dada su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Compra> deleteCompra(@PathVariable Long idProducto, @PathVariable Long idCompra){
        Compra c=compraService.deleteCompra(idProducto, idCompra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
