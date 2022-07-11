package com.mentoring.microservices.songservice.mapper;

import com.mentoring.microservices.songservice.dto.SongDto;
import com.mentoring.microservices.songservice.entity.Song;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {
    public Song toEntity(SongDto songDto) {
        return Song.builder()
                .resourceId(songDto.getResourceId())
                .name(songDto.getName())
                .artist(songDto.getArtist())
                .album(songDto.getAlbum())
                .length(songDto.getLength())
                .year(songDto.getYear())
                .build();
    }
    public SongDto toDto(Song song) {
        return SongDto.builder()
                .id(song.getId())
                .resourceId(song.getResourceId())
                .name(song.getName())
                .artist(song.getArtist())
                .album(song.getAlbum())
                .length(song.getLength())
                .year(song.getYear())
                .build();
    }
}
