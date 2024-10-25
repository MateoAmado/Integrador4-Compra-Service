package org.compra.controller;

import org.compra.model.Compra;
import org.compra.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @PutMapping("/{idProducto}/{idCliente}")
    public ResponseEntity<Compra> updateCompra(@PathVariable Long idProducto, @PathVariable Long idCompra, @RequestBody Compra compra){
        Compra c=compraService.updateCompra(idProducto, idCompra, compra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Compra> deleteCompra(@PathVariable Long idProducto, @PathVariable Long idCompra){
        Compra c=compraService.deleteCompra(idProducto, idCompra);
        if(c!=null){
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
