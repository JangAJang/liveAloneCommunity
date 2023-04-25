package com.capstone.chat.repository;

import com.capstone.chat.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {

    Flux<Chat> findAllBySenderAndReceiver(String sender, String receiver);
}
