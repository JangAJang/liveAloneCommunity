package com.capstone.liveAloneComunity.repository.post;

import com.capstone.liveAloneComunity.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByTitle(String title);
}
