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
@RequestMapping("/api/requestlines")
public class RequestLineController {
	
	@Autowired
	private RequestLineRepositroy reqLRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	private boolean recalculateRequestTotal(@PathVariable int requestId){
		Optional<Request> aRequest = reqRepo.findById(requestId);
		if(aRequest.isEmpty()) {
			return false;
		}
		Request request = aRequest.get();
		Iterable<RequestLine> requestLines = reqLRepo.findByRequestId(requestId);
		double grandTotal = 0;
		for(RequestLine rl : requestLines) {
			var lineTotal = rl.getQuantity() * rl.getProduct().getPrice();
			grandTotal += lineTotal;
		}
		request.setTotal(grandTotal);
		reqRepo.save(request);
		return true;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<RequestLine>> getRequestLines(){
		Iterable<RequestLine> requestLines = reqLRepo.findAll();
		return new ResponseEntity<Iterable<RequestLine>>(requestLines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<RequestLine> getRequestLine(@PathVariable int id){
		Optional<RequestLine> requestLine = reqLRepo.findById(id);
		if(requestLine.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RequestLine>(requestLine.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<RequestLine> postRequestLine(@RequestBody RequestLine requestLine){
		RequestLine newRequestLine = reqLRepo.save(requestLine);
		Optional<Request> Request = reqRepo.findById(requestLine.getRequest().getId());
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
	public ResponseEntity putRequestLine(@PathVariable int id, @RequestBody RequestLine requestLine) {
		if(requestLine.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqLRepo.save(requestLine);
		Optional<Request> Request = reqRepo.findById(requestLine.getRequest().getId());
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
		Optional<RequestLine> requestLine = reqLRepo.findById(id);
		if(requestLine.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqLRepo.delete(requestLine.get());
		Optional<Request> Request = reqRepo.findById(requestLine.get().getRequest().getId());
		if(!Request.isEmpty()) {
			boolean success = recalculateRequestTotal(Request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
