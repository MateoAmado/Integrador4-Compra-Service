package org.compra.controller;

import org.compra.model.Compra;
import org.compra.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompraController {
    @Autowired
    private CompraService compraService;

    @GetMapping()
    public ResponseEntity<List<Compra>> listar() {
        List<Compra> compras=compraService.getCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> getCompra(@PathVariable Long idProducto, @PathVariable Long idCompra) {
        Compra compra=compraService.findById(idProducto, idCompra);
        if(compra!=null){
            return new ResponseEntity<>(compra, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Compra> postCompra(@RequestBody Compra compra) {
        Compra compraGuardada = compraService.save(compra);
        if(compraGuardada != null) {
            return ResponseEntity.ok(compraGuardada);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
