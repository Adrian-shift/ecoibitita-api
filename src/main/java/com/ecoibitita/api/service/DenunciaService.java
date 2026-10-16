package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.NovaDenunciaRequest;
import com.ecoibitita.api.model.ConfiguracaoSistema;
import com.ecoibitita.api.model.denuncia.*;
import com.ecoibitita.api.model.enums.TipoImagem;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.CategoriaResiduoRepository;
import com.ecoibitita.api.repository.ConfiguracaoSistemaRepository;
import com.ecoibitita.api.repository.DenunciaRepository;
import com.ecoibitita.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class DenunciaService {

    private static final int MAX_FOTOS = 3;

    private final DenunciaRepository denunciaRepository;
    private final CategoriaResiduoRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoSistemaRepository configuracaoRepository;
    private final ArmazenamentoImagemService armazenamento;

    public DenunciaService(DenunciaRepository denunciaRepository,
                           CategoriaResiduoRepository categoriaRepository,
                           UsuarioRepository usuarioRepository,
                           ConfiguracaoSistemaRepository configuracaoRepository,
                           ArmazenamentoImagemService armazenamento) {
        this.denunciaRepository = denunciaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.armazenamento = armazenamento;
    }

    // Sem @Transactional de propósito: o save() confirma no banco dentro do try,
    // assim, se algo falhar, conseguimos apagar do R2 as fotos já enviadas.
    public Denuncia registrar(NovaDenunciaRequest req, List<MultipartFile> fotos) {
        validarFotos(fotos);

        CategoriaResiduo categoria = categoriaRepository.findById(req.categoriaId())
                .filter(CategoriaResiduo::isAtivo)
                .orElseThrow(() -> erro(HttpStatus.BAD_REQUEST, "Categoria inválida ou inativa"));

        Usuario cidadao = resolverCidadao(req.usuarioId());

        Localizacao loc = new Localizacao();
        loc.setLatitude(req.latitude());
        loc.setLongitude(req.longitude());
        loc.setEndereco(req.endereco());
        loc.setBairro(req.bairro());
        loc.setPontoReferencia(req.pontoReferencia());

        Denuncia denuncia = new Denuncia();
        denuncia.setCategoria(categoria);
        denuncia.setCidadao(cidadao);
        denuncia.setLocalizacao(loc);
        denuncia.setDescricao(req.descricao());

        List<ArmazenamentoImagemService.ArquivoSalvo> enviados = new ArrayList<>();
        try {
            for (MultipartFile foto : fotos) {
                var salvo = armazenamento.salvar(foto, denuncia.getProtocolo());
                enviados.add(salvo);

                Imagem imagem = new Imagem();
                imagem.setUrlImagem(salvo.url());
                imagem.setChaveArmazenamento(salvo.chave());
                imagem.setTipoImagem(TipoImagem.OCORRENCIA);
                imagem.setEnviadoPor(cidadao);
                denuncia.adicionarImagem(imagem);
            }
            return denunciaRepository.save(denuncia);

        } catch (IllegalArgumentException e) {
            apagarEnviados(enviados);
            throw erro(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            apagarEnviados(enviados);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível registrar a denúncia", e);
        }
    }

    private void validarFotos(List<MultipartFile> fotos) {
        if (fotos == null || fotos.isEmpty() || fotos.stream().allMatch(MultipartFile::isEmpty)) {
            throw erro(HttpStatus.BAD_REQUEST, "É obrigatório enviar pelo menos 1 foto");
        }
        if (fotos.size() > MAX_FOTOS) {
            throw erro(HttpStatus.BAD_REQUEST, "O máximo é de " + MAX_FOTOS + " fotos por denúncia");
        }
        if (fotos.stream().anyMatch(MultipartFile::isEmpty)) {
            throw erro(HttpStatus.BAD_REQUEST, "Há um arquivo vazio entre as fotos");
        }
    }

    private Usuario resolverCidadao(Integer usuarioId) {
        if (usuarioId != null) {
            return usuarioRepository.findById(usuarioId)
                    .filter(Usuario::isAtivo)
                    .orElseThrow(() -> erro(HttpStatus.BAD_REQUEST, "Usuário inválido ou inativo"));
        }
        boolean anonimaHabilitada = configuracaoRepository
                .findByChave(ConfiguracaoSistema.DENUNCIA_ANONIMA_HABILITADA)
                .map(ConfiguracaoSistema::valorComoBoolean)
                .orElse(false);
        if (!anonimaHabilitada) {
            throw erro(HttpStatus.FORBIDDEN, "Denúncias anônimas estão desabilitadas. Faça login para denunciar.");
        }
        return null;
    }

    private void apagarEnviados(List<ArmazenamentoImagemService.ArquivoSalvo> enviados) {
        for (var arquivo : enviados) {
            try {
                armazenamento.excluir(arquivo.chave());
            } catch (Exception ignorado) {
                // melhor tentativa: se não apagar agora, a foto fica órfã no bucket
            }
        }
    }

    private ResponseStatusException erro(HttpStatus status, String mensagem) {
        return new ResponseStatusException(status, mensagem);
    }
}