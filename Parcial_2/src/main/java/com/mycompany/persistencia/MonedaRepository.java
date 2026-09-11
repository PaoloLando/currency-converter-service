package com.mycompany.persistencia;

import com.mycompany.modelo.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long> {
    Optional<Moneda> findByNombreIgnoreCase(String nombre);
}