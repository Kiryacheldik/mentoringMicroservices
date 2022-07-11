package com.mentoring.microservices.resourceservice.mapper;

import com.mentoring.microservices.resourceservice.dto.ResourceCreateRequest;
import com.mentoring.microservices.resourceservice.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceRequestMapper implements EntityRequestMapper<ResourceCreateRequest, Resource> {
    @Override
    public Resource toEntity(ResourceCreateRequest dto) {
        return Resource.builder().location(dto.getLocation()).build();
    }

    @Override
    public List<Resource> toEntity(List<ResourceCreateRequest> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
