package com.mentoring.microservices.resourceservice.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.mentoring.microservices.resourceservice.dto.ResourceCreateRequest;
import com.mentoring.microservices.resourceservice.dto.ResourceCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mentoring.microservices.resourceservice.service.ResourceService;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {

    @Autowired
    public ResourceController(ResourceService service) {
        this.service = service;
    }

    private final ResourceService service;

    @GetMapping("/resources")
    public List<ResourceCreateResponse> getResources() {
        return service.getResourceList();
    }

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<ResourceCreateResponse> getResource(@PathVariable("resourceId") Long resourceId) {
        return ResponseEntity.ok().body(service.getResource(resourceId));
    }

    @PostMapping(value = "/resources", consumes = { MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResourceCreateResponse> saveResource(@RequestParam String location, @RequestPart MultipartFile song) throws URISyntaxException {
        ResourceCreateResponse result = service.saveResource(new ResourceCreateRequest(location, song));
        return ResponseEntity.created(new URI("/api/resources/" + result.getId()))
                .body(result);
    }

    @DeleteMapping("resources/{resourceId}")
    public ResponseEntity<ResourceCreateResponse> deleteResource(@PathVariable("resourceId") Long resourceId) {
        service.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}
