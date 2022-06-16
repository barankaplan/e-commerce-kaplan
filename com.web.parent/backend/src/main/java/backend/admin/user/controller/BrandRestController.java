package backend.admin.user.controller;


import backend.admin.user.service.BrandService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {
    private final BrandService brandService;

    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }
    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id")Long id,@Param("name") String name){
        return brandService.checkUnique(id, name);
    }
}
