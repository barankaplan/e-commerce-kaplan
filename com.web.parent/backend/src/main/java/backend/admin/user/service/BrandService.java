package backend.admin.user.service;


import backend.admin.user.exceptions.BrandNotFoundException;
import backend.admin.user.repository.BrandRepository;
import common.data.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    public static final int BRANDS_PER_PAGE = 10;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll(){
        return brandRepository.findAllBrands();
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

    public String checkUnique (Long id,String name){
        boolean isCreatingNew=(id==null || id==0);
        Brand brandByName=brandRepository.findByName(name);
        if (isCreatingNew){
            if (brandByName != null) return "Duplicate";
        }else{
            if (brandByName != null && brandByName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }

    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }



}
