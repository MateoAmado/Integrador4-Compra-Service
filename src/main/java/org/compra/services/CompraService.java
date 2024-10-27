package org.compra.services;

import org.compra.model.Compra;
import org.compra.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }

    public Compra findById(Long idProducto, Long idCompra) {
        return compraRepository.findById(idProducto, idCompra);
    }

    public Compra save(Compra compra, jakarta.servlet.http.HttpServletRequest request) {

        // Obtén el encabezado de autorización
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrae el token
            String token = authHeader.substring(7);

            // Crea los encabezados para las solicitudes externas
            HttpHeaders headers = createHeaders(token);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);


            ResponseEntity<String> validationResponseC = validateReponse("http://localhost:8010/clientes/" + compra.getIdCliente(), entity);

            ResponseEntity<String> validationResponseP = validateReponse("http://localhost:8070/productos/" + compra.getIdProducto(), entity);

            // Si ambas respuestas son exitosas, guarda la compra
            if (validationResponseC.getStatusCode() == HttpStatus.OK &&
                    validationResponseP.getStatusCode() == HttpStatus.OK) {
                return compraRepository.save(compra);
            }
        }

        // Si el token no es válido o las validaciones fallan, devuelve null o lanza una excepción
        throw new RuntimeException("No tienes permisos para realizar esta operación o el recurso no existe");
    }


    public ResponseEntity<String> validateReponse(String url, HttpEntity<String> entity) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

    }

    public Compra updateCompra(Long idProducto, Long idCliente, Compra compra, jakarta.servlet.http.HttpServletRequest request) {

        // Obtén el encabezado de autorización
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrae el token

            String token = authHeader.substring(7);

            // Crea los encabezados para las solicitudes externas
            HttpHeaders headers = createHeaders(token);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);


            ResponseEntity<String> validationResponseC = validateReponse("http://localhost:8010/clientes/" + idCliente, entity);

            ResponseEntity<String> validationResponseP = validateReponse("http://localhost:8070/productos/" + idProducto, entity);

            // Si ambas respuestas son exitosas, guarda la compra

            if (validationResponseC.getStatusCode() == HttpStatus.OK &&
                    validationResponseP.getStatusCode() == HttpStatus.OK) {
                Compra c = compraRepository.findById(idProducto, idCliente);
                if (c != null) {

                    if((compra.getIdCliente() != null && compra.getIdProducto() != null) &&
                            (compra.getIdCliente() != c.getIdCliente() || compra.getIdProducto() != c.getIdProducto())) {
                        compraRepository.deleteBy(c.getIdProducto(), c.getIdCliente());
                        Compra compra1 = new Compra();
                        compra1.setIdProducto(compra.getIdProducto());
                        compra1.setIdCliente(compra.getIdCliente());
                        compra1.setCantidad(compra.getCantidad());
                        compra1.setFecha(compra.getFecha());
                        return compraRepository.save(compra1);
                    }
                    else {
                        c.setCantidad(compra.getCantidad());
                        c.setFecha(compra.getFecha());
                        return compraRepository.save(c);
                    }
                }
            }
        }

        throw new RuntimeException("No tienes permisos para realizar esta operación o el recurso no existe");
    }

    public Compra deleteCompra(Long idProducto, Long idCliente) {
        System.out.println("Entro al emtodo service");
        Compra compra = compraRepository.findById(idProducto, idCliente);
        if(compra!=null) {
            compraRepository.deleteBy(idProducto, idCliente);
            return compra;
        }
        return null;
    }


    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return headers;
    }
}