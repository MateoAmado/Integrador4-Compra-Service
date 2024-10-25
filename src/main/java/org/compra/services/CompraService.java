package org.compra.services;

import org.compra.model.Compra;
import org.compra.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public List<Compra> getCompras(){
        return compraRepository.findAll();
    }

    public Compra findById(Long idProducto, Long idCompra) {
        return compraRepository.findById(idProducto, idCompra);
    }

    public Compra save(Compra compra) {
        String apiDeValen = "http://localhost:8070/productos/"+compra.getIdProducto();
        String apiDeMate = "http://localhost:8010/clientes/"+compra.getIdCliente();
        if(restTemplate.getForEntity(apiDeValen, String.class)!=null && restTemplate.getForEntity(apiDeMate, String.class)!=null){
            return compraRepository.save(compra);
        }
        return null;
    }

    public Compra updateCompra(Long idProducto, Long idCompra, Compra compra) {
        String apiDeValen = "http://localhost:8070/productos/"+compra.getIdProducto();
        String apiDeMate = "http://localhost:8010/clientes/"+compra.getIdCliente();
        if(restTemplate.getForEntity(apiDeValen, String.class)!=null && restTemplate.getForEntity(apiDeMate, String.class)!=null){
            Compra c=compraRepository.findById(idProducto, idCompra);
            if(c!=null) {
                c.setCantidad(compra.getCantidad());
                c.setFecha(compra.getFecha());
                c.setIdProducto(compra.getIdProducto());
                c.setIdCliente(compra.getIdCliente());
                return compraRepository.save(c);
            }}
        return null;
    }

    public Compra deleteCompra(Long idProducto, Long idCliente) {
        Compra compra = compraRepository.findById(idProducto, idCliente);
        if(compra!=null) {
            compraRepository.delete(compra);
            return compra;
        }
        return null;
    }
}
