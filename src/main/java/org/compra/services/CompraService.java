package org.compra.services;

import org.compra.model.Compra;
import org.compra.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        String apiDeValen = "http://localhost:8070/productos/"+compra.getIdProducto();
        if(restTemplate.getForEntity(apiDeValen, String.class)!=null){
            return compraRepository.save(compra);
        }
        return null;
    }


    public Compra updateCompra(Long IdCliente, Long idProducto, Long idCompra, Compra compra) {
        String apiDeValen = "http://localhost:8070/productos/"+compra.getIdProducto();
        String APICliente = "http://localhost:8010/clientes/"+compra.getIdCliente();
        if(restTemplate.getForEntity(apiDeValen, String.class)!=null && restTemplate.getForEntity(APICliente, String.class)!=null){
            Compra c=compraRepository.findById(idProducto, IdCliente);
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

    public Long productoMasVendido() {
        return compraRepository.productoMasVendido();
    }
}
