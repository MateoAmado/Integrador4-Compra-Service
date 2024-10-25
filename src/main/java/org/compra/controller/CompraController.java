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
@RequestMapping("/compras")
@Tag(name = "Compras", description = "Operaciones CRUD relacionadas con las compras")
public class CompraController {
    @Autowired
    private CompraService compraService;

    @Operation(summary = "Obtener todas las compras", description = "Devuelve una lista de todas las compras registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compras obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<List<Compra>> listar() {
        List<Compra> compras=compraService.getCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @Operation(summary = "Obtener compra por id", description = "Devuelve la compra solicitada mediante el idProducto e idCliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> getCompra(@PathVariable Long idProducto, @PathVariable Long idCompra) {
        Compra compra=compraService.findById(idProducto, idCompra);
        if(compra!=null){
            return new ResponseEntity<>(compra, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Crear compra", description = "Crea la compra que el usuario envía en el body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra creada con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Compra> postCompra(@RequestBody Compra compra) {
        Compra compraGuardada = compraService.save(compra);
        if(compraGuardada != null) {
            return ResponseEntity.ok(compraGuardada);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Actualizar compra por id", description = "Actualiza la compra solicitada con idProducto e idCliente enviados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> updateCompra(@PathVariable Long idProducto, @PathVariable Long idCompra, @RequestBody Compra compra){
        Compra c=compraService.updateCompra(idProducto, idCompra, compra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Obtener compra por id", description = "Devuelve la compra solicitada mediante el idProducto e idCliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Compra borrada con éxito"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> deleteCompra(@PathVariable Long idProducto, @PathVariable Long idCompra){
        Compra c=compraService.deleteCompra(idProducto, idCompra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
