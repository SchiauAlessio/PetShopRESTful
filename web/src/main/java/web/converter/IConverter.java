package web.converter;

import web.dto.BaseDto;
import core.model.BaseEntity;

import java.io.Serializable;

public interface IConverter<ID extends Serializable, Model extends BaseEntity<ID>, Dto extends BaseDto<ID>> {
    Model convertDtoToModel(Dto dto);

    Dto convertModelToDto(Model model);
}
