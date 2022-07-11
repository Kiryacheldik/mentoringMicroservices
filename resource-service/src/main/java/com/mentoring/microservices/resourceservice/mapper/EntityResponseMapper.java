package com.mentoring.microservices.resourceservice.mapper;

import java.util.List;

public interface EntityResponseMapper<D, E> {

    D toDto(E entity);

    List <D> toDto(List<E> entityList);
}
