package com.capstone.liveAloneCommunity.service.post;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.dto.post.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.exception.post.NotMyPostException;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostResponseDto writePost(Member member, WritePostRequestDto writePostRequestDto){
        Post post = Post.builder()
                .title(new Title(writePostRequestDto.getTitle()))
                .content(new Content(writePostRequestDto.getContent()))
                .member(member)
                .category(writePostRequestDto.getCategory()).build();
        postRepository.save(post);
        return PostResponseDto.toDto(post);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto searchPost(SearchPostRequestDto searchPostRequestDto){
        Page<PostResponseDto> searchResult = postRepository
                .searchPost(searchPostRequestDto);
        return new MultiPostResponseDto(searchResult);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto getMembersPost(MembersPostRequestDto membersPostRequestDto){
        Member member = memberRepository.findMemberById(membersPostRequestDto.getId())
                .orElseThrow(MemberNotFoundException::new);
        Page<Post> membersPost = postRepository
                .findAllByMemberOrderByCreatedDateDesc(member, PageRequest.of(membersPostRequestDto.getPage(), membersPostRequestDto.getSize()));
        Page<PostResponseDto> result = membersPost.map(i-> PostResponseDto.toDto(i, member));
        return new MultiPostResponseDto(result);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto getPostByCategory(PostByCategoryRequestDto postByCategoryRequestDto){
        Page<PostResponseDto> postByCategory = postRepository.getPostByCategory(postByCategoryRequestDto);
        return new MultiPostResponseDto(postByCategory);
    }

    private Pageable getPageRequestOfPostByCategory(PostByCategoryRequestDto postByCategoryRequestDto) {
        return PageRequest.of(postByCategoryRequestDto.getPage(), postByCategoryRequestDto.getSize(),
                Sort.by("CreatedDate").descending());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id){
        return PostResponseDto.toDto(getPostById(id));
    }

    public PostResponseDto editPost(EditPostRequestDto editPostRequestDto, Member currentMember){
        Post post = getPostById(editPostRequestDto.getId());
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
        if(!post.isWriter(member)) throw new MemberNotAllowedException();
    }

    public void isMyPost(Member member, Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if(post.isWriter(member)) return;
        throw new NotMyPostException();
    }
}
