package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditCommentRequestDto {
    @NotNull(message = "댓글의 id를 입력해주세요.")
    private Long commentId;
    @NotBlank(message = "변경할 댓글의 내용을 입력해주세요.")
    private String modifyContent;
}
