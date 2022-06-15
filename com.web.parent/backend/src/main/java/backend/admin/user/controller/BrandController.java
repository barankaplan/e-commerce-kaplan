package backend.admin.user.controller;


import backend.admin.user.service.BrandService;
import common.data.entity.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public String listAll(Model model){
       List<Brand> listBrands= brandService.listAll();
       model.addAttribute("listBrands",listBrands);
       return "brands/brands";
    }
}
