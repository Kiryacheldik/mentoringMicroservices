package com.mentoring.microservices.resourceservice.mapper;

import com.mentoring.microservices.resourceservice.dto.ResourceCreateResponse;
import com.mentoring.microservices.resourceservice.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceResponseMapper implements EntityResponseMapper<ResourceCreateResponse, Resource> {
    @Override
    public ResourceCreateResponse toDto(Resource entity) {
        return ResourceCreateResponse.builder().id(entity.getId()).location(entity.getLocation()).build();
    }

    @Override
    public List<ResourceCreateResponse> toDto(List<Resource> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
