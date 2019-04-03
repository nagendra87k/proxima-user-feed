package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts,Long> {


}
