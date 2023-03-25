package com.capstone.liveAloneCommunity.service.category;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.dto.category.CategoryRequestDto;
import com.capstone.liveAloneCommunity.entity.category.Category;
import com.capstone.liveAloneCommunity.exception.category.CategoryNotFoundException;
import com.capstone.liveAloneCommunity.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryRequestDto categoryRequestDto, Optional<Category> category){
        if(category.isEmpty()) {
            categoryRepository.save(new Category(new Title(categoryRequestDto.getTitle()), new Content(categoryRequestDto.getContent()), null));
            return;
        }
        categoryRepository.save(new Category(new Title(categoryRequestDto.getTitle()), new Content(categoryRequestDto.getContent()), category.get()));
    }

    public void editCategory(Long id, CategoryRequestDto categoryRequestDto){
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.edit(categoryRequestDto);
    }
}
