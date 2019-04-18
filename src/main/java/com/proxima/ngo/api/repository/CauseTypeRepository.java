package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.CauseType;
import com.proxima.ngo.api.model.Causes;
import com.proxima.ngo.api.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CauseTypeRepository extends JpaRepository<CauseType,Long> {
    Optional<CauseType> findByTitle(String type);
}
