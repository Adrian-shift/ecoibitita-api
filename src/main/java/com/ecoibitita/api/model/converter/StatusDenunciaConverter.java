package com.ecoibitita.api.model.converter;

import com.ecoibitita.model.enums.StatusDenuncia;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusDenunciaConverter implements AttributeConverter<StatusDenuncia, String> {

    @Override
    public String convertToDatabaseColumn(StatusDenuncia status) {
        return status == null ? null : status.getDescricao();
    }

    @Override
    public StatusDenuncia convertToEntityAttribute(String valor) {
        return valor == null ? null : StatusDenuncia.deDescricao(valor);
    }
}
