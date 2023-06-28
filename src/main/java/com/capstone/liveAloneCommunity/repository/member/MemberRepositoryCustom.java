package com.capstone.liveAloneCommunity.repository.member;

import com.capstone.liveAloneCommunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneCommunity.dto.member.SearchMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<MemberResponseDto> searchMember(final SearchMemberDto searchMemberDto);
}
