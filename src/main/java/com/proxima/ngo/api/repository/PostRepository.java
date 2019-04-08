package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Posts,Long> {

    @Query("select c from Posts c where c.createdAt < CURRENT_DATE ")
    List<Posts> findAllPosts(Pageable pageable);

}
