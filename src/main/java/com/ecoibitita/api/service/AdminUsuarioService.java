package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.EditarUsuarioRequest;
import com.ecoibitita.api.dto.NovoUsuarioRequest;
import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoAdminResponse;
import com.ecoibitita.api.dto.UsuarioAdminResponse;
import com.ecoibitita.api.model.usuario.Perfil;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.CategoriaResiduoRepository;
import com.ecoibitita.api.repository.PerfilRepository;
import com.ecoibitita.api.repository.RegistroAuditoriaRepository;
import com.ecoibitita.api.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PerfilRepository perfilRepository;
    private final CategoriaResiduoRepository categoriaRepository;
    private final RegistroAuditoriaRepository auditoriaRepository;
    private final AuditoriaService auditoria;
    private final PasswordEncoder passwordEncoder;

    public AdminUsuarioService(UsuarioRepository usuarioRepository,
                               PerfilRepository perfilRepository,
                               CategoriaResiduoRepository categoriaRepository,
                               RegistroAuditoriaRepository auditoriaRepository,
                               AuditoriaService auditoria,
                               PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.categoriaRepository = categoriaRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.auditoria = auditoria;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- leitura ----------

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
            throw erro(HttpStatus.BAD_REQUEST, "Perfil inválido");
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

    // ---------- criar e editar ----------

    @Transactional
    public UsuarioAdminResponse criar(NovoUsuarioRequest req, Integer idAutor) {
        Usuario autor = autor(idAutor);
        Perfil perfil = perfilDaGestao(req.perfil());
        String email = req.email().trim().toLowerCase();

        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw erro(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        Usuario novo = new Usuario();
        novo.setPerfil(perfil);
        novo.setNome(req.nome().trim());
        novo.setEmail(email);
        novo.setSenhaHash(passwordEncoder.encode(req.senha()));
        try {
            novo = usuarioRepository.saveAndFlush(novo);
        } catch (DataIntegrityViolationException e) {
            throw erro(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        auditoria.registrar(autor, "CRIAR_USUARIO", "Usuario", novo.getId(), "Perfil: " + perfil.getNome());
        return UsuarioAdminResponse.de(novo);
    }

    @Transactional
    public UsuarioAdminResponse editar(Integer id, EditarUsuarioRequest req, Integer idAutor) {
        Usuario autor = autor(idAutor);
        Usuario alvo = usuarioRepository.findById(id)
                .filter(u -> PERFIS_GESTAO.contains(u.getPerfil().getNome()))
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        Perfil novoPerfil = perfilDaGestao(req.perfil());

        boolean mudouPerfil = !novoPerfil.getNome().equals(alvo.getPerfil().getNome());
        boolean mudouStatus = req.ativo() != alvo.isAtivo();

        // ninguém se tranca para fora do sistema
        if (alvo.getId().equals(autor.getId()) && (mudouPerfil || !req.ativo())) {
            throw erro(HttpStatus.BAD_REQUEST, "Você não pode desativar a sua conta nem alterar o seu perfil");
        }
        boolean eraAdminAtivo = alvo.isAtivo() && alvo.temPerfil(Perfil.ADMINISTRADOR);
        boolean continuaAdminAtivo = req.ativo() && Perfil.ADMINISTRADOR.equals(novoPerfil.getNome());
        if (eraAdminAtivo && !continuaAdminAtivo
                && usuarioRepository.countByAtivoTrueAndPerfilNomeIn(List.of(Perfil.ADMINISTRADOR)) <= 1) {
            throw erro(HttpStatus.BAD_REQUEST, "É necessário manter pelo menos um administrador ativo");
        }

        List<String> alteracoes = new ArrayList<>();

        String nome = req.nome().trim();
        if (!nome.equals(alvo.getNome())) {
            alvo.setNome(nome);
            alteracoes.add("nome");
        }
        if (mudouPerfil) {
            alteracoes.add("perfil " + alvo.getPerfil().getNome() + " -> " + novoPerfil.getNome());
            alvo.setPerfil(novoPerfil);
        }
        if (mudouStatus) {
            if (req.ativo()) alvo.ativar(); else alvo.desativar();
            alteracoes.add(req.ativo() ? "ativado" : "desativado");
        }
        if (req.novaSenha() != null && !req.novaSenha().isBlank()) {
            alvo.setSenhaHash(passwordEncoder.encode(req.novaSenha()));
            alteracoes.add("senha redefinida");
        }

        if (!alteracoes.isEmpty()) {
            alvo.setAtualizadoEm(OffsetDateTime.now());
            // a auditoria guarda o que mudou, nunca a senha
            auditoria.registrar(autor, "EDITAR_USUARIO", "Usuario", alvo.getId(), String.join("; ", alteracoes));
        }
        return UsuarioAdminResponse.de(alvo);
    }

    private Usuario autor(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> erro(HttpStatus.UNAUTHORIZED, "Sessão inválida"));
    }

    private Perfil perfilDaGestao(String nome) {
        if (nome == null || !PERFIS_GESTAO.contains(nome)) {
            throw erro(HttpStatus.BAD_REQUEST, "Perfil inválido");
        }
        return perfilRepository.findByNome(nome)
                .orElseThrow(() -> erro(HttpStatus.BAD_REQUEST, "Perfil inválido"));
    }

    private ResponseStatusException erro(HttpStatus status, String mensagem) {
        return new ResponseStatusException(status, mensagem);
    }
}