package com.capstone.chat.repository.chat;

import com.capstone.chat.entity.chat.Chat;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {

    Flux<Chat> findByChatRoomOrderBySentAtAsc(ChatRoom chatRoom);
}
