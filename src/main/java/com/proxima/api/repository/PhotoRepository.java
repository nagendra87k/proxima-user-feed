package com.proxima.api.repository;

import com.proxima.api.model.Photos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photos,Long> {
}
