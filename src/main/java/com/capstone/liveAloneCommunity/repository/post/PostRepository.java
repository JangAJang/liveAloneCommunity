package com.capstone.liveAloneCommunity.repository.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByTitle_Title(String title);

    Page<Post> findAllByCategoryOrderByCreatedDateDesc(Category category, Pageable pageable);
    Page<Post> findAllByMemberOrderByCreatedDateDesc(Member member, Pageable pageable);
}
