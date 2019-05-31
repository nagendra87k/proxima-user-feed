package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Causes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CauseRepository extends PagingAndSortingRepository<Causes,Long> {

    List<Causes> findByEmail(String email);
    void deleteById(Long id);
    Optional<Causes> findById(Long id);
    List<Causes> findAllByEmail(String email, Pageable pageable);

    @Query("SELECT c FROM Causes c WHERE c.id=id")
    Causes findCausesById(@Param("id") Long id);



    @Override
    List<Causes> findAll(Sort sort);

    List<Causes> findAllByEmail(String email);



    @Query("SELECT c.title,c.email,c.description,c.posts FROM Causes c INNER JOIN c.posts p")
    List<Causes> fetchCausePostData();
}
