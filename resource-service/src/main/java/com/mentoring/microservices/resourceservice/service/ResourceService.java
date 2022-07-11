package com.mentoring.microservices.resourceservice.service;

import com.mentoring.microservices.resourceservice.dto.ResourceCreateRequest;
import com.mentoring.microservices.resourceservice.dto.ResourceCreateResponse;
import com.mentoring.microservices.resourceservice.entity.Resource;
import com.mentoring.microservices.resourceservice.mapper.ResourceRequestMapper;
import com.mentoring.microservices.resourceservice.mapper.ResourceResponseMapper;
import com.mentoring.microservices.resourceservice.repository.ResourceRepository;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Value("${config.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.routing-keys.resource-create-routing-key}")
    private String resourceCreateRoutingKey;

    @Autowired
    public ResourceService(ResourceRepository repository, ResourceRequestMapper requestMapper, ResourceResponseMapper responseMapper, S3Client s3Client, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.s3Client = s3Client;
        this.rabbitTemplate = rabbitTemplate;
    }

    private final ResourceRepository repository;
    private final ResourceRequestMapper requestMapper;
    private final ResourceResponseMapper responseMapper;
    private final S3Client s3Client;
    private final RabbitTemplate rabbitTemplate;

    public List<ResourceCreateResponse> getResourceList() {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    public ResourceCreateResponse getResource(Long id) {
        Resource resource = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(resource.getLocation()).build();
        byte[] data = s3Client.getObject(getObjectRequest).readAllBytes();
        ResourceCreateResponse resourceResponse = responseMapper.toDto(resource);
        resourceResponse.setResourceBytes(data);
        return resourceResponse;
    }

    @SneakyThrows
    public ResourceCreateResponse saveResource(ResourceCreateRequest resourceRequest) {
        // save in storage
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(resourceRequest.getLocation()).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(resourceRequest.getFile().getBytes()));
        // save in DB
        final Resource resource = repository.save(requestMapper.toEntity(resourceRequest));
        final ResourceCreateResponse response = responseMapper.toDto(resource);
        response.setResourceBytes(resourceRequest.getFile().getBytes());
        // send to mq
        rabbitTemplate.convertAndSend(exchange, resourceCreateRoutingKey, response.getId());
        return response;
    }

    public void deleteResource(Long id) {
        // get object from DB
        Optional<Resource> resourceOpt = repository.findById(id);
        if (resourceOpt.isEmpty()) {
            return;
        }
        Resource resource = resourceOpt.get();

        // delete from storage
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(resource.getLocation()).build();
        s3Client.deleteObject(deleteObjectRequest);
        // delete from DB
        repository.deleteById(id);
    }

}
