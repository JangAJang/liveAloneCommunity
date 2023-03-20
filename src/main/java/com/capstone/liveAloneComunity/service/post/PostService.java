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

    public PostResponseDto writePost(Member member, WritePostRequestDto writePostRequestDto){
        Category category = categoryRepository.findById(writePostRequestDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        Post post = Post.builder()
                .title(new Title(writePostRequestDto.getTitle()))
                .content(new Content(writePostRequestDto.getContent()))
                .member(member)
                .category(category).build();
        postRepository.save(post);
        return PostResponseDto.toDto(post);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto searchPost(SearchPostRequestDto searchPostRequestDto
            , Pageable pageable){
        Page<PostResponseDto> searchResult = postRepository
                .searchPost(searchPostRequestDto, pageable);
        return new MultiPostResponseDto(searchResult);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto getMembersPost(Pageable pageable, Long id){
        Page<PostResponseDto> membersPost = postRepository.getMembersPost(id, pageable);
        return new MultiPostResponseDto(membersPost);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id){
        return PostResponseDto.toDto(getPostById(id));
    }

    public PostResponseDto editPost(EditPostRequestDto editPostRequestDto, Member currentMember, Long id){
        Post post = getPostById(id);
        validatePostAuthority(currentMember, post);
        post.editTitle(new Title(editPostRequestDto.getTitle()));
        post.editContent(new Content(editPostRequestDto.getContent()));
        return PostResponseDto.toDto(post);
    }

    public void deletePost(Long id, Member currentMember){
        Post post = getPostById(id);
        validatePostAuthority(currentMember, post);
        postRepository.delete(post);
    }

    private Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public void validatePostAuthority(Member member, Post post){
        if(!post.getMember().equals(member)) throw new MemberNotAllowedException();
    }
}
