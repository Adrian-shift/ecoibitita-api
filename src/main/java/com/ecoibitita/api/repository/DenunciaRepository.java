package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Integer>,
        JpaSpecificationExecutor<Denuncia> {

    List<Denuncia> findByStatus(StatusDenuncia status);

    List<Denuncia> findByLocalizacaoBairro(String bairro);

    Optional<Denuncia> findByProtocolo(String protocolo);

    List<Denuncia> findByCidadaoIdOrderByDataRegistroDesc(Integer usuarioId);

    List<Denuncia> findByProtocoloIn(Collection<String> protocolos);

    @Query("select d.status, count(d) from Denuncia d group by d.status")
    List<Object[]> contarPorStatus();

    @Query("select d.localizacao.bairro, count(d) from Denuncia d group by d.localizacao.bairro")
    List<Object[]> contarPorBairro();

    // ---------- apoio ao dashboard do fiscal ----------

    long countByDataRegistroGreaterThanEqualAndDataRegistroLessThan(OffsetDateTime inicio, OffsetDateTime fim);

    /** Cada item: [0] = bairro, [1] = total de abertas (nem resolvidas, nem canceladas), maior primeiro. */
    @Query("select d.localizacao.bairro, count(d) from Denuncia d "
            + "where d.status <> :a and d.status <> :b "
            + "group by d.localizacao.bairro order by count(d) desc, d.localizacao.bairro")
    List<Object[]> contarAbertasPorBairro(@Param("a") StatusDenuncia a, @Param("b") StatusDenuncia b);

    @Query("select distinct d.localizacao.bairro from Denuncia d order by d.localizacao.bairro")
    List<String> listarBairros();
}