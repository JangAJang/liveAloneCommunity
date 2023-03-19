package com.capstone.liveAloneComunity.service.post;

import com.capstone.liveAloneComunity.domain.post.Content;
import com.capstone.liveAloneComunity.domain.post.Title;
import com.capstone.liveAloneComunity.dto.post.*;
import com.capstone.liveAloneComunity.entity.category.Category;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.entity.post.Post;
import com.capstone.liveAloneComunity.exception.category.CategoryNotFoundException;
import com.capstone.liveAloneComunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneComunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneComunity.repository.category.CategoryRepository;
import com.capstone.liveAloneComunity.repository.post.PostRepository;
import com.capstone.liveAloneComunity.repository.post.SearchPostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public MultiPostResponseDto searchPost(SearchPostRequestDto searchPostRequestDto
            , Pageable pageable, SearchPostType searchPostType){
        Page<PostResponseDto> searchResult = postRepository
                .searchPost(searchPostRequestDto, searchPostType, pageable);
        return new MultiPostResponseDto(searchResult);
    }

    public MultiPostResponseDto getMembersPost(Pageable pageable, Long id){
        Page<PostResponseDto> membersPost = postRepository.getMembersPost(id, pageable);
        return new MultiPostResponseDto(membersPost);
    }

    public PostResponseDto editPost(EditPostRequestDto editPostRequestDto, Member currentMember, Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        validatePostAuthority(currentMember, post);
        post.editTitle(new Title(editPostRequestDto.getTitle()));
        post.editContent(new Content(editPostRequestDto.getContent()));
        return PostResponseDto.toDto(post);
    }

    public void validatePostAuthority(Member member, Post post){
        if(!post.getMember().equals(member)) throw new MemberNotAllowedException();
    }
}
