package com.java.prs.capstone.project.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductRepository prodRepo;
	
	
	@GetMapping
	public ResponseEntity<Iterable<Product>> getProducts(){
		Iterable<Product> products = prodRepo.findAll();
		return new ResponseEntity<Iterable<Product>>(products, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id){
		Optional<Product> product = prodRepo.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Product> postProduct(@RequestBody Product product){
		Product newProduct = prodRepo.save(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putProduct(@PathVariable int id, @RequestBody Product product) {
		if(product.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		prodRepo.save(product);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteProduct(@PathVariable int id) {
		Optional<Product> product = prodRepo.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		prodRepo.delete(product.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
