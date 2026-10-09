package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {
    List<Denuncia> findByStatus(String status);
    List<Denuncia> findByLocalizacaoBairro(String bairro);
}