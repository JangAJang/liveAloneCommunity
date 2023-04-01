package com.capstone.liveAloneCommunity.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthService {

    private StringBuilder authNum;

    private void createCode(){
        authNum = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 8).forEach(i -> authNum.append(createRandomCharacter(random)));
    }

    private char createRandomCharacter(Random random){
        int value = random.nextInt(3);
        if(value == 0) return (char) (random.nextInt(26)+97);
        if(value == 1) return (char) (random.nextInt(26)+65);
        return (char) (random.nextInt(9));
    }
}