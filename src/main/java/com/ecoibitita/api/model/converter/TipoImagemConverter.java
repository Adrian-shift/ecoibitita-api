package com.ecoibitita.model.converter;

import com.ecoibitita.model.enums.TipoImagem;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoImagemConverter implements AttributeConverter<TipoImagem, String> {

    @Override
    public String convertToDatabaseColumn(TipoImagem tipo) {
        return tipo == null ? null : tipo.getDescricao();
    }

    @Override
    public TipoImagem convertToEntityAttribute(String valor) {
        return valor == null ? null : TipoImagem.deDescricao(valor);
    }
}
