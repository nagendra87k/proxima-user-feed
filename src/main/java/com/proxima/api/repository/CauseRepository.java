package com.proxima.api.repository;

import com.proxima.api.model.Causes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauseRepository extends JpaRepository<Causes,Long> {
}
