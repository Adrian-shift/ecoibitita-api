package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.HistoricoDenuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoDenunciaRepository extends JpaRepository<HistoricoDenuncia, Integer> {

    /** Cada item: [0] = data de registro da denúncia, [1] = data em que chegou ao status. */
    @Query("select h.denuncia.dataRegistro, h.alteradoEm from HistoricoDenuncia h "
            + "where h.statusNovo = :status")
    List<Object[]> datasAteOStatus(@Param("status") StatusDenuncia status);
}
