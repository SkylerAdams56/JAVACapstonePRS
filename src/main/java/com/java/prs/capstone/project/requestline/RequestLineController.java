package com.java.prs.capstone.project.requestline;

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

import com.java.prs.capstone.project.request.Request;
import com.java.prs.capstone.project.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/RequestLines")
public class RequestLineController {
	
	@Autowired
	private RequestLineRepositroy reqLRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	private boolean recalculateRequestTotal(@PathVariable int RequestId){
		Optional<Request> anRequest = reqRepo.findById(RequestId);
		if(anRequest.isEmpty()) {
			return false;
		}
		Request Request = anRequest.get();
		Iterable<RequestLine> RequestLines = reqLRepo.findByRequestId(RequestId);
		double grandTotal = 0;
		for(RequestLine rl : RequestLines) {
			var lineTotal = rl.getQuantity() * rl.getProduct().getPrice();
			grandTotal += lineTotal;
		}
		Request.setTotal(grandTotal);
		reqRepo.save(Request);
		return true;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<RequestLine>> getRequestLines(){
		Iterable<RequestLine> RequestLines = reqLRepo.findAll();
		return new ResponseEntity<Iterable<RequestLine>>(RequestLines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<RequestLine> getRequestLine(@PathVariable int id){
		Optional<RequestLine> RequestLine = reqLRepo.findById(id);
		if(RequestLine.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RequestLine>(RequestLine.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<RequestLine> postRequestLine(@RequestBody RequestLine RequestLine){
		RequestLine newRequestLine = reqLRepo.save(RequestLine);
		Optional<Request> Request = reqRepo.findById(RequestLine.getRequest().getId());
		if(!Request.isEmpty()) {
			boolean success = recalculateRequestTotal(Request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<RequestLine>(newRequestLine, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestLine(@PathVariable int id, @RequestBody RequestLine RequestLine) {
		if(RequestLine.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqLRepo.save(RequestLine);
		Optional<Request> Request = reqRepo.findById(RequestLine.getRequest().getId());
		if(!Request.isEmpty()) {
			boolean success = recalculateRequestTotal(Request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestLine(@PathVariable int id) {
		Optional<RequestLine> RequestLine = reqLRepo.findById(id);
		if(RequestLine.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqLRepo.delete(RequestLine.get());
		Optional<Request> Request = reqRepo.findById(RequestLine.get().getRequest().getId());
		if(!Request.isEmpty()) {
			boolean success = recalculateRequestTotal(Request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
