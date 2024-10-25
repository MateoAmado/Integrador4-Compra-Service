package org.compra.repository;

import org.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{
    @Query("SELECT c FROM Compra c WHERE c.idProducto=:idProducto AND c.idCliente = :idCompra")
    public Compra findById(Long idProducto, Long idCompra);
}
