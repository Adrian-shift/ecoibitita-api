package com.ecoibitita.model.denuncia;

import com.ecoibitita.model.converter.TipoImagemConverter;
import com.ecoibitita.model.enums.TipoImagem;
import com.ecoibitita.model.usuario.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "imagens_denuncia")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "denuncia_id")
    private Denuncia denuncia;

    @Column(name = "url_imagem", nullable = false, length = 500)
    private String urlImagem;

    @Column(name = "public_id_cloudinary", length = 150)
    private String publicIdCloudinary;

    @Convert(converter = TipoImagemConverter.class)
    @Column(name = "tipo_foto", length = 20)
    private TipoImagem tipoImagem = TipoImagem.OCORRENCIA;

    @ManyToOne
    @JoinColumn(name = "enviado_por")
    private Usuario enviadoPor;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getPublicIdCloudinary() {
        return publicIdCloudinary;
    }

    public void setPublicIdCloudinary(String publicIdCloudinary) {
        this.publicIdCloudinary = publicIdCloudinary;
    }

    public TipoImagem getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(TipoImagem tipoImagem) {
        this.tipoImagem = tipoImagem;
    }

    public Usuario getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(Usuario enviadoPor) {
        this.enviadoPor = enviadoPor;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
