package com.capstone.liveAloneComunity.repository.post;

import com.capstone.liveAloneComunity.dto.post.PostResponseDto;
import com.capstone.liveAloneComunity.dto.post.SearchPostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostResponseDto> searchPost(SearchPostRequestDto searchPostRequestDto, Pageable pageable);

    Page<PostResponseDto> getMembersPost(Long id, Pageable pageable);
}
