package com.mentoring.microservices.resourceservice.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCreateRequest {
    @NotNull
    private String location;

    @NotNull
    private MultipartFile file;
}
