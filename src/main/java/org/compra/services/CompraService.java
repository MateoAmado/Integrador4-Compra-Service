package org.compra.services;

import org.compra.model.Compra;
import org.compra.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        String apiDeValen = "http://localhost:8065/{"+compra.getIdProducto()+"}";
        if(restTemplate.getForEntity(apiDeValen, String.class)!=null){
            return compraRepository.save(compra);
        }
        return null;
    }
}
