package com.capstone.liveAloneComunity.service.category;

import com.capstone.liveAloneComunity.domain.post.Content;
import com.capstone.liveAloneComunity.domain.post.Title;
import com.capstone.liveAloneComunity.dto.category.CategoryRequestDto;
import com.capstone.liveAloneComunity.entity.category.Category;
import com.capstone.liveAloneComunity.exception.category.CategoryNotFoundException;
import com.capstone.liveAloneComunity.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryRequestDto categoryRequestDto, Optional<Category> category){
        categoryRepository.save(new Category(new Title(categoryRequestDto.getTitle()), new Content(categoryRequestDto.getContent()), category.get()));
    }

    public void editCategory(Long id, CategoryRequestDto categoryRequestDto){
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.edit(categoryRequestDto);
    }
}