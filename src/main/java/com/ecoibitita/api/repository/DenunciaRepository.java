package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.service.ArmazenamentoImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.*;

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


}