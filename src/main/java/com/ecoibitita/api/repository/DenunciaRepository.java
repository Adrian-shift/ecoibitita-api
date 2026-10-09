package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    List<Denuncia> findByStatus(String status);
    List<Denuncia> findByBairro(String bairro);
}