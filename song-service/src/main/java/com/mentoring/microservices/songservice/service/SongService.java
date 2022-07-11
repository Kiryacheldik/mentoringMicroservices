package com.mentoring.microservices.songservice.service;

import com.mentoring.microservices.songservice.dto.SongDto;
import com.mentoring.microservices.songservice.entity.Song;
import com.mentoring.microservices.songservice.mapper.SongMapper;
import com.mentoring.microservices.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {

    @Autowired
    public SongService(SongRepository repository, SongMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private final SongRepository repository;
    private final SongMapper mapper;

    public List<SongDto> getSongList() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public SongDto getSongById(Long songId) {
        return repository.findById(songId)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public SongDto saveSong(SongDto songDto) {
        final Song song = mapper.toEntity(songDto);
        return mapper.toDto(repository.save(song));
    }

    public void deleteSong(Long songId) {
        repository.deleteById(songId);
    }
}
