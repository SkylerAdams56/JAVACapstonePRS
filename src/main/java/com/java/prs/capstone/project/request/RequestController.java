package com.java.prs.capstone.project.request;

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
@RequestMapping("/api/requests")
public class RequestController {
	
	private final String Status_New = "NEW";
	private final String Status_Review = "REVIEW";
	private final String Status_Approved = "APPROVED";
	private final String Status_Rejected = "REJECTED";
	
	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping("reviews")
	public ResponseEntity<Iterable<Request>> getRequestsInReview(){
		Iterable<Request> requestsInReview = reqRepo.findByStatus(Status_Review);
		return new ResponseEntity<Iterable<Request>>(requestsInReview, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Request>> getRequests(){
		Iterable<Request> requests = reqRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Request> getRequest(@PathVariable int id){
		Optional<Request> request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Request> postRequest(@RequestBody Request request){
		Request newRequest = reqRepo.save(request);
		return new ResponseEntity<Request>(newRequest, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity reviewRequest(@PathVariable int id, @RequestBody Request request) {
		String newStatus = request.getTotal() <= 100 ? Status_Approved : Status_Review;
		request.setStatus(newStatus);
		return putRequest(id, request);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity approveRequest(@PathVariable int id, @RequestBody Request request) {
		request.setStatus(Status_Approved);
		return putRequest(id, request);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity rejectRequest(@PathVariable int id, @RequestBody Request request) {
		request.setStatus(Status_Rejected);
		return putRequest(id, request);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequest(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqRepo.save(request);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequest(@PathVariable int id) {
		Optional<Request> request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqRepo.delete(request.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
