package com.tripwhiz.tripwhizuserback.category.service;

import com.tripwhiz.tripwhizuserback.category.dto.SubCategoryDTO;
import com.tripwhiz.tripwhizuserback.category.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }
    public List<SubCategoryDTO> getSubCategoriesByCategory(Long cno) {
        return subCategoryRepository.findByCategory_Cno(cno).stream()
                .map(SubCategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
