package com.java.prs.capstone.project.vendor;

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
@RequestMapping("/api/vendors")
public class VendorController {
	@Autowired
	private VendorRepository venRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Vendor>> getVendors(){
		Iterable<Vendor> Vendors = venRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(Vendors, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable int id){
		Optional<Vendor> Vendor = venRepo.findById(id);
		if(Vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(Vendor.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor){
		Vendor newVendor = venRepo.save(vendor);
		return new ResponseEntity<Vendor>(newVendor, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		if(vendor.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		venRepo.save(vendor);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteVendor(@PathVariable int id) {
		Optional<Vendor> vendor = venRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		venRepo.delete(vendor.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
