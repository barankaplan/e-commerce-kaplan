package backend.admin.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import backend.admin.user.repository.ProductRepository;
import common.data.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias().isEmpty() || product.getAlias() == null) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        }else {
            product.setAlias(product.getAlias().replaceAll(" ","-"));
        }
        product.setUpdatedTime(new Date());

         return productRepository.save(product);
    }

    public String checkUnique(Long id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Optional<Product> productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) return "Duplicate";
        } else {
            if (productByName != null && productByName.get().getId() != id) {
                return "Duplicate";
            }
        }

        return "OK";
    }

}
