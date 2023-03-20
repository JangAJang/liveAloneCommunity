package com.capstone.liveAloneComunity.repository.category;

import com.capstone.liveAloneComunity.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle_Title(String title);
}
