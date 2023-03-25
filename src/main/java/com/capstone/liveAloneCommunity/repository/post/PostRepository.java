package com.capstone.liveAloneCommunity.repository.post;

import com.capstone.liveAloneCommunity.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByTitle_Title(String title);
}
