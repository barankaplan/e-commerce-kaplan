package backend.admin.user.service;


import backend.admin.user.exceptions.BrandNotFoundException;
import backend.admin.user.repository.BrandRepository;
import common.data.entity.Brand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll(){
        return brandRepository.findAll();
    }

    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }
    public Brand  get(Long id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Could not find any brand with ID "+id);
        }
    }

    public void delete(Long id) throws BrandNotFoundException {
        Long countById=brandRepository.countById(id);
        if (countById==null||countById==0){
            throw new BrandNotFoundException("Could not find any brand with ID "+id);
        }
        brandRepository.deleteById(id);
    }


}
