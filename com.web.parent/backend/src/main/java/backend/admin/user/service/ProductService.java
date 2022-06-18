package backend.admin.user.service;

import java.util.List;

import backend.admin.user.repository.ProductRepository;
import common.data.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

	@Autowired private ProductRepository productRepository;
	
	public List<Product> listAll() {
		return  productRepository.findAll();
	}
}
