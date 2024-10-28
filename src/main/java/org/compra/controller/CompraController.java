package org.compra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.compra.dto.compraClienteDto;
import org.compra.model.Compra;
import org.compra.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Operation(summary = "Obtener informe de montos por cliente", description = "Devuelve un informe con el monto total de compras realizadas por cada cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informe generado con éxito"),
            @ApiResponse(responseCode = "500", description = "Error al generar el informe")
    })
    @GetMapping("/informe")
    public ResponseEntity<Map<String, Double>> obtenerInforme(jakarta.servlet.http.HttpServletRequest request) {
        try {
            Map<String, Double> informeMontosCliente = compraService.getInformeClientesMonto(request);
            return ResponseEntity.ok(informeMontosCliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener el producto más vendido", description = "Devuelve el producto que ha sido más vendido y su cantidad total.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto más vendido obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontró información de productos vendidos"),
            @ApiResponse(responseCode = "500", description = "Error al obtener el producto más vendido")
    })
    @GetMapping("/masVendido")
    public ResponseEntity<String> getMasVendido(jakarta.servlet.http.HttpServletRequest request) {
        try {
            Optional<String> masVendido = compraService.obtenerProductoMasVendido(request);
            return masVendido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener informe de ventas por día", description = "Devuelve un informe con el monto total de ventas realizadas cada día.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informe de ventas por día generado con éxito"),
            @ApiResponse(responseCode = "500", description = "Error al generar el informe de ventas por día")
    })
    @GetMapping("/informeDia")
    public ResponseEntity<Map<LocalDate, Double>> obtenerInformePorDia(jakarta.servlet.http.HttpServletRequest request) {
        try {
            Map<LocalDate, Double> ventasPorDia = compraService.generarReporteVentasPorDia(request);
            return ResponseEntity.ok(ventasPorDia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<Compra> postCompra(@RequestBody Compra compra, jakarta.servlet.http.HttpServletRequest request) {
        Compra compraGuardada = compraService.save(compra, request);
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
    public ResponseEntity<Compra> updateCompra(@PathVariable Long idProducto, @PathVariable Long idCliente, @RequestBody Compra compra, jakarta.servlet.http.HttpServletRequest request){
        Compra c=compraService.updateCompra(idProducto, idCliente, compra, request);
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
    public ResponseEntity<Compra> deleteCompra(@PathVariable Long idProducto, @PathVariable Long idCliente){
        System.out.println("Entro al metodo");
        Compra c=compraService.deleteCompra(idProducto, idCliente);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}