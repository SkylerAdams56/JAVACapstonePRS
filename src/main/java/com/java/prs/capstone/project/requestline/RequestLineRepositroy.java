package com.java.prs.capstone.project.requestline;

import org.springframework.data.repository.CrudRepository;

public interface RequestLineRepositroy extends CrudRepository<RequestLine, Integer>{
	Iterable<RequestLine> findByRequestId(int requestId);
}
