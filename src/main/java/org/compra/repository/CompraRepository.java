package org.compra.repository;

import org.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{
    @Query("SELECT c FROM Compra c WHERE c.idProducto=:idProducto AND c.idCliente = :idCliente")
    public Compra findById(Long idProducto, Long idCliente);

    @Modifying
    @Transactional
    @Query("DELETE FROM Compra c WHERE c.idProducto=:idProducto AND c.idCliente = :idCliente")
    public void deleteBy(Long idProducto, Long idCliente);


    @Query("SELECT c.idProducto, SUM(c.cantidad) AS totalVendido FROM Compra c GROUP BY c.idProducto ORDER BY totalVendido DESC")
    List<Object[]> productoMasVendido();
}
