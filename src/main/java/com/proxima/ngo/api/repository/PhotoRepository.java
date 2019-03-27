package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Photos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photos,Long> {
}
