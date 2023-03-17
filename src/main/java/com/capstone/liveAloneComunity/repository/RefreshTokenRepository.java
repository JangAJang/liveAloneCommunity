package com.capstone.liveAloneComunity.repository;

import com.capstone.liveAloneComunity.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
