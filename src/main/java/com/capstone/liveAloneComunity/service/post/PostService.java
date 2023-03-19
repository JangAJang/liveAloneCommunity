package com.capstone.liveAloneComunity.service.post;

import com.capstone.liveAloneComunity.domain.post.Content;
import com.capstone.liveAloneComunity.domain.post.Title;
import com.capstone.liveAloneComunity.dto.post.PostResponseDto;
import com.capstone.liveAloneComunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneComunity.entity.category.Category;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.entity.post.Post;
import com.capstone.liveAloneComunity.exception.category.CategoryNotFoundException;
import com.capstone.liveAloneComunity.repository.category.CategoryRepository;
import com.capstone.liveAloneComunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public PostResponseDto writePost(Member member, WritePostRequestDto writePostRequestDto, Long id){
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        Post post = Post.builder()
                .title(new Title(writePostRequestDto.getTitle()))
                .content(new Content(writePostRequestDto.getContent()))
                .member(member)
                .category(category).build();
        postRepository.save(post);
        return PostResponseDto.toDto(post);
    }
}
