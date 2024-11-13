package com.tripwhiz.tripwhizuserback.category.controller;

import com.tripwhiz.tripwhizuserback.category.domain.Category;
import com.tripwhiz.tripwhizuserback.category.dto.CategoryDTO;
import com.tripwhiz.tripwhizuserback.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        log.info("getAllCategories");

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

}

