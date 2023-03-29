package com.java.prs.capstone.project.request;

import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer>{
	Iterable<Request> findByStatus(String status);
}
