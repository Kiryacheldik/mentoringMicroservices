package com.mentoring.microservices.processorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongResourceDto {
    private Long id;
    private String location;
    private byte[] resourceBytes;
}
