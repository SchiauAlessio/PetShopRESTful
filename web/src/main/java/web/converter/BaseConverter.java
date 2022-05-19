package web.converter;

import web.dto.BaseDto;
import core.model.BaseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseConverter<ID extends Serializable, Model extends BaseEntity<ID>, Dto extends BaseDto<ID>> implements IConverter<ID, Model, Dto> {
    public Set<ID> convertModelsToIDs(Set<Model> models) {
        return models.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
    }

    public Set<ID> convertDtosToIDs(Set<Dto> dtos) {
        return dtos.stream()
                .map(BaseDto::getId)
                .collect(Collectors.toSet());
    }

    public LinkedHashSet<Dto> convertModelsToDtos(Collection<Model> models) {
        return models.stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public LinkedHashSet<Model> convertDtosToModels(Collection<Dto> dtos) {
        return dtos.stream()
                .map(this::convertDtoToModel)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
