package com.mentoring.microservices.resourceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceCreateResponse {
    private Long id;
    private String location;
    private byte[] resourceBytes;
}
