package com.mentoring.microservices.processorservice.util;

import com.mentoring.microservices.processorservice.entity.SongDto;
import lombok.SneakyThrows;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class SongDataParser {
    @SneakyThrows
    public SongDto parseSong(byte[] songBytes) {
        Map<String, String> songInfo = new HashMap<>();

        InputStream input = new ByteArrayInputStream(songBytes);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();

        // List all metadata
        String[] metadataNames = metadata.names();

        for(String name : metadataNames){
            songInfo.put(name, metadata.get(name));
        }

        return SongDto.builder()
                .name(songInfo.get("title"))
                .artist(songInfo.get("meta:author"))
                .album(songInfo.get("xmpDM:album"))
                .length(Long.parseLong(songInfo.get("samplerate")))
                .year(songInfo.get("xmpDM:releaseDate"))
                .build();
    }
}
