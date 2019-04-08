package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images,Long> {
}
