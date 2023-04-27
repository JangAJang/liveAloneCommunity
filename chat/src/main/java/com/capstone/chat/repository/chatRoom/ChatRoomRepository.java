package com.capstone.chat.repository.chatRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long> {
}
