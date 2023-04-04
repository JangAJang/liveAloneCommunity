package com.capstone.liveAloneCommunity.service.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseCleanup{
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;
}
