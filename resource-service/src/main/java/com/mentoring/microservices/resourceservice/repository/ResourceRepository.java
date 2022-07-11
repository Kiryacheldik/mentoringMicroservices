package com.mentoring.microservices.resourceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mentoring.microservices.resourceservice.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
