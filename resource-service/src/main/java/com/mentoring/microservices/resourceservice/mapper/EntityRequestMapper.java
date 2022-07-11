package com.mentoring.microservices.resourceservice.mapper;

import java.util.List;

public interface EntityRequestMapper<D, E> {

    E toEntity(D dto);

    List<E> toEntity(List<D> dtoList);
}
