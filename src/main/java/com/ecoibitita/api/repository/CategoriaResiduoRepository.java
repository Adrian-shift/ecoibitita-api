package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.CategoriaResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaResiduoRepository extends JpaRepository<CategoriaResiduo, Integer> {
    List<CategoriaResiduo> findByAtivoTrueOrderByNome();
    boolean existsByNomeIgnoreCase(String nome);
}
