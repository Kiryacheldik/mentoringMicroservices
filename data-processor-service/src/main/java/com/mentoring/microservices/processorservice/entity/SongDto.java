package com.mentoring.microservices.processorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongDto {
    private Long resourceId;
    private String name;
    private String artist;
    private String album;
    private Long length;
    private String year;
}
