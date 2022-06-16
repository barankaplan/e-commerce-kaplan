package backend.admin.user.service;

import java.util.List;

import backend.admin.user.repository.ProductRepository;
import common.data.entity.Product;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

	 private final ProductRepository repo;

	public ProductService(ProductRepository repo) {
		this.repo = repo;
	}

	public List<Product> listAll() {
		return  repo.findAll();
	}
}
