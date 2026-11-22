package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoAdminResponse;
import com.ecoibitita.api.dto.UsuarioAdminResponse;
import com.ecoibitita.api.model.usuario.Perfil;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.CategoriaResiduoRepository;
import com.ecoibitita.api.repository.RegistroAuditoriaRepository;
import com.ecoibitita.api.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminUsuarioService {

    private static final ZoneId FUSO = ZoneId.of("America/Bahia");
    private static final int MAX_TAMANHO_PAGINA = 50;
    // o perfil Cidadão existe no banco, mas não é usado na gestão
    private static final List<String> PERFIS_GESTAO = List.of(Perfil.ADMINISTRADOR, Perfil.FISCAL);

    private final UsuarioRepository usuarioRepository;
    private final CategoriaResiduoRepository categoriaRepository;
    private final RegistroAuditoriaRepository auditoriaRepository;

    public AdminUsuarioService(UsuarioRepository usuarioRepository,
                               CategoriaResiduoRepository categoriaRepository,
                               RegistroAuditoriaRepository auditoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(readOnly = true)
    public ResumoAdminResponse resumo() {
        OffsetDateTime inicioDoMes = LocalDate.now(FUSO).withDayOfMonth(1).atStartOfDay(FUSO).toOffsetDateTime();
        OffsetDateTime inicioDoDia = LocalDate.now(FUSO).atStartOfDay(FUSO).toOffsetDateTime();

        return new ResumoAdminResponse(
                usuarioRepository.countByPerfilNomeIn(PERFIS_GESTAO),
                usuarioRepository.countByPerfilNome(Perfil.ADMINISTRADOR),
                usuarioRepository.countByPerfilNome(Perfil.FISCAL),
                usuarioRepository.countByAtivoTrueAndPerfilNomeIn(PERFIS_GESTAO),
                usuarioRepository.countByPerfilNomeInAndCriadoEmGreaterThanEqual(PERFIS_GESTAO, inicioDoMes),
                categoriaRepository.countByAtivoTrue(),
                // "alterações registradas": os acessos (LOGIN) não entram na conta
                auditoriaRepository.countByDataHoraGreaterThanEqualAndAcaoNot(inicioDoDia, "LOGIN"));
    }

    @Transactional(readOnly = true)
    public PaginaResponse<UsuarioAdminResponse> listar(String busca, String perfil, Boolean ativo,
                                                       int pagina, int tamanho) {
        if (perfil != null && !perfil.isBlank() && !PERFIS_GESTAO.contains(perfil)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil inválido");
        }
        int p = Math.max(pagina, 0);
        int t = Math.min(Math.max(tamanho, 1), MAX_TAMANHO_PAGINA);

        // filtros opcionais: o que vier vazio é ignorado
        Specification<Usuario> filtros = (root, query, cb) -> {
            List<Predicate> condicoes = new ArrayList<>();
            condicoes.add(root.get("perfil").get("nome").in(PERFIS_GESTAO));
            if (busca != null && !busca.isBlank()) {
                String termo = "%" + busca.trim().toLowerCase() + "%";
                condicoes.add(cb.or(
                        cb.like(cb.lower(root.get("nome")), termo),
                        cb.like(cb.lower(root.get("email")), termo)));
            }
            if (perfil != null && !perfil.isBlank()) {
                condicoes.add(cb.equal(root.get("perfil").get("nome"), perfil));
            }
            if (ativo != null) {
                condicoes.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(condicoes.toArray(new Predicate[0]));
        };

        Page<Usuario> resultado = usuarioRepository.findAll(
                filtros, PageRequest.of(p, t, Sort.by(Sort.Direction.ASC, "nome")));

        return new PaginaResponse<>(
                resultado.getContent().stream().map(UsuarioAdminResponse::de).toList(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.isLast());
    }
}