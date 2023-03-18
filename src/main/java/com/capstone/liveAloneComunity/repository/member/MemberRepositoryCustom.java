package com.capstone.liveAloneComunity.repository.member;

import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.dto.member.SearchMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<MemberResponseDto> searchMember(SearchMemberDto searchMemberDto, Pageable pageable);
}
