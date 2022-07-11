package com.mentoring.microservices.songservice.controller;

import com.mentoring.microservices.songservice.dto.SongDto;
import com.mentoring.microservices.songservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SongController {
    @Autowired
    public SongController(SongService service) {
        this.service = service;
    }

    private final SongService service;

    @GetMapping("/songs")
    public List<SongDto> getSongs() {
        return service.getSongList();
    }

    @GetMapping("/songs/{songId}")
    public ResponseEntity<SongDto> getSong(@PathVariable("songId") Long songId) {
        return ResponseEntity.ok().body(service.getSongById(songId));
    }

    @PostMapping("/songs")
    public ResponseEntity<SongDto> saveSong(@RequestBody SongDto songDto) throws URISyntaxException {
        SongDto result = service.saveSong(songDto);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
                .body(result);
    }

    @DeleteMapping("songs/{songId}")
    public ResponseEntity<SongDto> deleteSong(@PathVariable("songId") Long songId) {
        service.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }
}
