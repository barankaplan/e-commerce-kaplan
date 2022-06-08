package backend.admin.user.service;


import backend.admin.user.repository.CategoryRepository;
import common.data.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private  final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<Category> listAll(){
        return categoryRepository.findAll();
    }
}
