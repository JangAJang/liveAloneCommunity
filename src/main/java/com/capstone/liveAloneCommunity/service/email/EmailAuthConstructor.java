package com.capstone.liveAloneCommunity.service.email;

import java.util.Random;
import java.util.stream.IntStream;

import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponentNumber.*;

public class EmailAuthConstructor {

    private StringBuilder authNum;

    public EmailAuthConstructor() {}

    public String getAuthNum(){
        authNum = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, EMAIL_AUTH_LENGTH.getValue()).forEach(i -> authNum.append(createRandomCharacter(random)));
        return authNum.toString();
    }

    private char createRandomCharacter(Random random){
        int value = random.nextInt(POSSIBLE_CASES.getValue());
        if(isLowerCase(value)) return getAlphabetInCase(LOWER_CASE_STARTER, random);
        if(isUpperCase(value)) return getAlphabetInCase(UPPER_CASE_STARTER, random);
        return getNumber(random);
    }

    private char getAlphabetInCase(EmailAuthComponentNumber emailAuthComponentNumber, Random random){
        return (char) (random.nextInt(ALPHABET_VARIES.getValue())+emailAuthComponentNumber.getValue());
    }

    private char getNumber(Random random){
        return (char) (random.nextInt(NUMBER_RANGE.getValue())+NUMBER_STARTER.getValue());
    }

    private boolean isUpperCase(int index){
        return index == UPPER_CASE_STARTER.getValue();
    }

    private boolean isLowerCase(int index){
        return index == LOWER_CASE_STARTER.getValue();
    }
}
