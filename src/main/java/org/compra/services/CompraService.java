package org.compra.services;

import org.compra.dto.Cliente;
import org.compra.dto.Producto;
import org.compra.dto.compraClienteDto;
import org.compra.model.Compra;
import org.compra.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
           
            String token = authHeader.substring(7);

            
            HttpHeaders headers = createHeaders(token);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);


            ResponseEntity<String> validationResponseC = validateReponse("http://localhost:8010/clientes/" + compra.getIdCliente(), entity);

            ResponseEntity<String> validationResponseP = validateReponse("http://localhost:8070/productos/" + compra.getIdProducto(), entity);

           
            if (validationResponseC.getStatusCode() == HttpStatus.OK &&
                    validationResponseP.getStatusCode() == HttpStatus.OK) {
                return compraRepository.save(compra);
            }
        }

       
        throw new RuntimeException("No tienes permisos para realizar esta operación o el recurso no existe");
    }


    public ResponseEntity<String> validateReponse(String url, HttpEntity<String> entity) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

    }

    public Map<String, Double> getInformeClientesMonto(jakarta.servlet.http.HttpServletRequest request){
        Map<String, Double> totalComprasPorCliente = new HashMap<>();


        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de autorización no encontrado o inválido");
        }
        String token = authHeader.substring(7);

        HttpHeaders headers = createHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

       
        List<Compra> compras = compraRepository.findAll();
        for (Compra compra : compras) {

            
            ResponseEntity<Cliente> clienteResponse = restTemplate.exchange(
                    "http://localhost:8010/clientes/" + compra.getIdCliente(),
                    HttpMethod.GET,
                    entity,
                    Cliente.class
            );
            Cliente cliente = clienteResponse.getBody();

            
            ResponseEntity<Producto> productoResponse = restTemplate.exchange(
                    "http://localhost:8070/productos/" + compra.getIdProducto(),
                    HttpMethod.GET,
                    entity,
                    Producto.class
            );
            Producto producto = productoResponse.getBody();

            
            double totalCompra = compra.getCantidad() * producto.getValor();

            
            totalComprasPorCliente.merge(cliente.getNombre(), totalCompra, Double::sum);
        }
        return totalComprasPorCliente;
    }


    public Map<LocalDate, Double> generarReporteVentasPorDia(jakarta.servlet.http.HttpServletRequest request) {
        Map<LocalDate, Double> ventasPorDia = new HashMap<>();

       
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de autorización no encontrado o inválido");
        }
        String token = authHeader.substring(7);

        HttpHeaders headers = createHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        
        List<Compra> compras = compraRepository.findAll();

        for (Compra compra : compras) {
            
            ResponseEntity<Producto> productoResponse = restTemplate.exchange(
                    "http://localhost:8070/productos/" + compra.getIdProducto(),
                    HttpMethod.GET,
                    entity,
                    Producto.class
            );
            Producto producto = productoResponse.getBody();

           
            double totalCompra = compra.getCantidad() * producto.getValor();

            
            LocalDate fechaCompra = compra.getFecha().toLocalDate();

            
            ventasPorDia.merge(fechaCompra, totalCompra, Double::sum);
        }
        return ventasPorDia;
    }


    public Optional<String> obtenerProductoMasVendido(jakarta.servlet.http.HttpServletRequest request) {
        
        List<Object[]> resultados = compraRepository.productoMasVendido();

        if (!resultados.isEmpty()) {
            Object[] productoMasVendido = resultados.get(0);
            Long idProducto = (Long) productoMasVendido[0];
            Long cantidadVendida = (Long) productoMasVendido[1];

            return Optional.of("Producto ID: " + idProducto + ", Cantidad Vendida: " + cantidadVendida);
        } else {
            return Optional.empty();
        }
    }

    public Compra updateCompra(Long idProducto, Long idCliente, Compra compra, jakarta.servlet.http.HttpServletRequest request) {

       
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            

            String token = authHeader.substring(7);

            
            HttpHeaders headers = createHeaders(token);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);


            ResponseEntity<String> validationResponseC = validateReponse("http://localhost:8010/clientes/" + idCliente, entity);

            ResponseEntity<String> validationResponseP = validateReponse("http://localhost:8070/productos/" + idProducto, entity);

          

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
