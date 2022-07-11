package com.mentoring.microservices.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDto {

    private Long id;

    @NotNull
    private Long resourceId;

    @NotNull
    private String name;

    @NotNull
    private String artist;

    @NotNull
    private String album;

    @NotNull
    private Long length;

    @NotNull
    private int year;
}
