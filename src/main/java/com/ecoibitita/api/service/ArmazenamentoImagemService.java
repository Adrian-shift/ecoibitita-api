package com.ecoibitita.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ArmazenamentoImagemService {

    // tipo permitido -> extensão do arquivo
    private static final Map<String, String> TIPOS_PERMITIDOS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );

    private final S3Client s3;
    private final String bucket;
    private final String publicUrl;

    public ArmazenamentoImagemService(S3Client s3,
                                      @Value("${r2.bucket}") String bucket,
                                      @Value("${r2.public-url}") String publicUrl) {
        this.s3 = s3;
        this.bucket = bucket;
        this.publicUrl = publicUrl;
    }

    public record ArquivoSalvo(String url, String chave) {}

    public ArquivoSalvo salvar(MultipartFile arquivo, String protocolo) throws IOException {
        String extensao = TIPOS_PERMITIDOS.get(arquivo.getContentType());
        if (extensao == null) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use JPG, PNG ou WebP.");
        }

        String chave = "denuncias/" + protocolo + "/" + UUID.randomUUID() + "." + extensao;

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(chave)
                        .contentType(arquivo.getContentType())
                        .build(),
                RequestBody.fromInputStream(arquivo.getInputStream(), arquivo.getSize()));

        return new ArquivoSalvo(publicUrl + "/" + chave, chave);
    }

    public void excluir(String chave) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .build());
    }
}
