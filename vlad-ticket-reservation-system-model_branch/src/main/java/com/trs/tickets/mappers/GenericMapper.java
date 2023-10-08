package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.BaseDto;
import com.trs.tickets.model.entity.BaseEntity;

import java.util.List;

public interface GenericMapper<E extends BaseEntity, D extends BaseDto> {
    E convert(D dto);
    D convert(E entity);

    default List<Long> map(List<? extends BaseEntity> value) {
        return value.stream().map(BaseEntity::getId).toList();
    }
}
