package backend.admin.user.service;


import backend.admin.user.repository.BrandRepository;
import common.data.entity.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll(){
        return brandRepository.findAll();
    }

}
