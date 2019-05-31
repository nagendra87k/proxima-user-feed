package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CauseTypeRepository extends JpaRepository<Type,Long> {

    Optional<Type> findAllById(Long id);
}
