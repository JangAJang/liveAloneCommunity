package com.capstone.liveAloneCommunity.repository.post;

import com.capstone.liveAloneCommunity.dto.post.MembersPostRequestDto;
import com.capstone.liveAloneCommunity.dto.post.PostByCategoryRequestDto;
import com.capstone.liveAloneCommunity.dto.post.PostResponseDto;
import com.capstone.liveAloneCommunity.dto.post.SearchPostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostResponseDto> searchPost(SearchPostRequestDto searchPostRequestDto);

    Page<PostResponseDto> getPostByCategory(PostByCategoryRequestDto postByCategoryRequestDto);
}
