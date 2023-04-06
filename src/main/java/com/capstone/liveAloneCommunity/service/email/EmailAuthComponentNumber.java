package com.capstone.liveAloneCommunity.service.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EmailAuthComponentNumber {

    POSSIBLE_CASES(3),
    LOWER_CASE_INDEX(0),
    UPPER_CASE_INDEX(1),
    NUMBER_RANGE(9),
    ALPHABET_VARIES(26),
    UPPER_CASE_STARTER(65),
    LOWER_CASE_STARTER(97),
    NUMBER_STARTER(48),
    EMAIL_AUTH_LENGTH(8);

    private final int value;
}
