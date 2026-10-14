package com.ecoibitita.dao;

import com.ecoibitita.model.denuncia.Denuncia;
import com.ecoibitita.model.enums.StatusDenuncia;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface DenunciaDAO extends GenericDAO<Denuncia, Integer> {

    Optional<Denuncia> buscarPorProtocolo(String protocolo);

    List<Denuncia> listarPorCidadao(Integer idUsuario);

    List<Denuncia> listarPorStatus(StatusDenuncia status);

    /**
     * Pesquisa com filtros opcionais (RF12). Parâmetro nulo = filtro ignorado.
     */
    List<Denuncia> pesquisar(String bairro, String cidade, Integer categoriaId,
                             OffsetDateTime dataInicial, OffsetDateTime dataFinal,
                             StatusDenuncia status);

    // ---------- apoio ao dashboard (RF14) ----------

    long contarTodas();

    long contarPorStatus(StatusDenuncia status);

    /** Cada item: [0] = bairro (String), [1] = total (Long), do maior para o menor. */
    List<Object[]> bairrosComMaisDenuncias(int limite);
}
