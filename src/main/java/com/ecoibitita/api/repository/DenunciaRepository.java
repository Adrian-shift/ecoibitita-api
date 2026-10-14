package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {

    List<Denuncia> findByStatus(StatusDenuncia status);

    List<Denuncia> findByLocalizacaoBairro(String bairro);

    Optional<Denuncia> findByProtocolo(String protocolo);

    List<Denuncia> findByCidadaoIdOrderByDataRegistroDesc(Integer usuarioId);

    @Query("select d.status, count(d) from Denuncia d group by d.status")
    List<Object[]> contarPorStatus();

    @Query("select d.localizacao.bairro, count(d) from Denuncia d group by d.localizacao.bairro")
    List<Object[]> contarPorBairro();
}