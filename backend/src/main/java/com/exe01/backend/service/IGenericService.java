package com.exe01.backend.service;

import org.springframework.http.ResponseEntity;

public interface IGenericService<T> {
    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> findAllByStatusTrue(int page, int limit);
    ResponseEntity<?> findAll(int page, int limit);

    ResponseEntity<?> save(T t);

    ResponseEntity<?> changeStatus(Long id);

    Boolean checkExist(Long id);

}
