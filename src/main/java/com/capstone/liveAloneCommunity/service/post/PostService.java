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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostResponseDto writePost(final Member member, final WritePostRequestDto writePostRequestDto) {
        Post post = Post.builder()
                .title(new Title(writePostRequestDto.getTitle()))
                .content(new Content(writePostRequestDto.getContent()))
                .member(member)
                .category(writePostRequestDto.getCategory()).build();

        postRepository.save(post);

        return PostResponseDto.from(post);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto searchPost(final SearchPostRequestDto searchPostRequestDto) {
        Page<PostResponseDto> searchResult = postRepository
                .searchPost(searchPostRequestDto);

        return new MultiPostResponseDto(searchResult);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto getMembersPost(final MembersPostRequestDto membersPostRequestDto) {
        Member member = memberRepository.findMemberById(membersPostRequestDto.getId())
                .orElseThrow(MemberNotFoundException::new);

        Page<Post> membersPost = postRepository
                .findAllByMemberOrderByCreatedDateDesc(member, PageRequest.of(membersPostRequestDto.getPage(), membersPostRequestDto.getSize()));

        Page<PostResponseDto> result = membersPost.map(i-> PostResponseDto.from(i, member));

        return new MultiPostResponseDto(result);
    }

    @Transactional(readOnly = true)
    public MultiPostResponseDto getPostByCategory(final PostByCategoryRequestDto postByCategoryRequestDto) {

        Page<PostResponseDto> postByCategory = postRepository.getPostByCategory(postByCategoryRequestDto);

        return new MultiPostResponseDto(postByCategory);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(final Long id) {
        return PostResponseDto.from(getPostById(id));
    }

    @Transactional
    public PostResponseDto editPost(final EditPostRequestDto editPostRequestDto, final Member currentMember) {
        Post post = getPostById(editPostRequestDto.getId());
        validatePostAuthority(currentMember, post);
        post.editTitle(new Title(editPostRequestDto.getTitle()));
        post.editContent(new Content(editPostRequestDto.getContent()));
        return PostResponseDto.from(post);
    }

    @Transactional
    public void deletePost(final Long id, final Member currentMember) {
        Post post = getPostById(id);
        validatePostAuthority(currentMember, post);
        postRepository.delete(post);
    }

    private Pageable getPageRequestOfPostByCategory(final PostByCategoryRequestDto postByCategoryRequestDto) {
        return PageRequest.of(postByCategoryRequestDto.getPage(), postByCategoryRequestDto.getSize(),
                Sort.by("CreatedDate").descending());
    }

    private Post getPostById(final Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public void validatePostAuthority(final Member member, final Post post) {
        if(!post.isWriter(member)) throw new MemberNotAllowedException();
    }

    public void isMyPost(final Member member, final Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if(post.isWriter(member)) return;
        throw new NotMyPostException();
    }
}
