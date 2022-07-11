package com.mentoring.microservices.processorservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongDto {
    private Long resourceId;
    private String name;
    private String artist;
    private String album;
    private Long length;
    private Integer year;
}
