package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.StudentConverter;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import com.exe01.backend.dto.request.student.UpdateStudentRequest;
import com.exe01.backend.entity.Student;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.StudentRepository;
import com.exe01.backend.repository.UniversityRepository;
import com.exe01.backend.service.IStudentService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.exe01.backend.constant.ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_STUDENT;

@Service
public class StudentServiceImpl implements IStudentService {

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public StudentDTO create(CreateStudentRequest request) {
        logger.info("Create student");

        Student student = new Student();
        student.setStudentCode(request.getStudentCode());
        student.setName(request.getName());
        student.setDob(request.getDob());
        student.setStatus(ConstStatus.ACTIVE_STATUS);

        var accountById = accountRepository.findById(request.getAccountId());
        boolean isAccountExist = accountById.isPresent() && accountById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

        if (!isAccountExist) {
            logger.warn("Account with id {} is not found", request.getAccountId());
            throw new EntityNotFoundException();
        }

        student.setAccount(accountById.get());

        var universityById = universityRepository.findById(request.getUniversityId());
        boolean isUniversityExist = universityById.isPresent() && universityById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

        if (!isUniversityExist) {
            logger.warn("University with id {} is not found", request.getUniversityId());
            throw new EntityNotFoundException();
        }

        student.setUniversity(universityById.get());

        studentRepository.save(student);

        return StudentConverter.toDto(student);
    }

    @Override
    public Boolean update(UUID id, UpdateStudentRequest request) {
        logger.info("Update student with id {}", id);
        var studentById = studentRepository.findById(id);
        boolean isStudentExist = studentById.isPresent();

        if (!isStudentExist) {
            logger.warn("Student with id {} is not found", id);
            throw new EntityNotFoundException();
        }


        var accountById = accountRepository.findById(request.getAccountId());
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            logger.warn("Account with id {} is not found", id);
            throw new EntityNotFoundException();
        }

        var universityById = universityRepository.findById(request.getUniversityId());
        boolean isUniversityExist = universityById.isPresent() && universityById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

        if (!isUniversityExist) {
            logger.warn("University with id {} is not found", request.getUniversityId());
            throw new EntityNotFoundException();
        }

        studentById.get().setId(id);
        studentById.get().setName(request.getName());
        studentById.get().setStudentCode(request.getStudentCode());
        studentById.get().setDob(request.getDob());
        studentById.get().setAccount(accountById.get());
        studentById.get().setUniversity(universityById.get());

        studentRepository.save(studentById.get());

        Set<String> keysToDelete = redisTemplate.keys("Student:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete student with id {}", id);
        var studentById = studentRepository.findById(id);
        boolean isStudentExist = studentById.isPresent();

        if (!isStudentExist) {
            logger.warn("Student with id {} is not found", id);
            throw new EntityNotFoundException();
        }

        studentById.get().setId(id);
        studentById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        studentRepository.save(studentById.get());

        Set<String> keysToDelete = redisTemplate.keys("Student:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public StudentDTO findById(UUID id) {
        logger.info("Find student by id {}", id);
        String hashKey = HASH_KEY_PREFIX_FOR_STUDENT + id.toString();
        StudentDTO studentDTO = (StudentDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, hashKey);

        if(!Objects.isNull(studentDTO)) {
            return studentDTO;
        }

        Optional<Student> studentById = studentRepository.findById(id);
        boolean isStudentExist = studentById.isPresent();

        if (!isStudentExist) {
            logger.warn("Student with id {} not found", id);
            throw new EntityNotFoundException();
        }

        studentDTO = StudentConverter.toDto(studentById.get());

        return studentDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all students with paging");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        String cacheKey = HASH_KEY_PREFIX_FOR_STUDENT + "all:" + page + ":" + limit;

        List<StudentDTO> studentDTOs;

        if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey)) {
            logger.info("Fetching students from cache for page {} and limit {}", page, limit);
            studentDTOs = (List<StudentDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey);
        } else {
            logger.info("Fetching students from database for page {} and limit {}", page, limit);
            List<Student> students = studentRepository.findAllByOrderByCreatedDate(pageable);
            studentDTOs = students.stream().map(StudentConverter::toDto).toList();

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey, studentDTOs);
        }

        result.setListResult(studentDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) studentRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all students with status is active");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        String cacheKey = HASH_KEY_PREFIX_FOR_STUDENT + "all:" + "active:" + page + ":" + limit;

        List<StudentDTO> studentDTOs;

        if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey)) {
            logger.info("Fetching students from cache for page {} and limit {}", page, limit);
            studentDTOs = (List<StudentDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey);
        } else {
            logger.info("Fetching students from database for page {} and limit {}", page, limit);
            List<Student> students = studentRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            studentDTOs = students.stream().map(StudentConverter::toDto).toList();

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey, studentDTOs);
        }

        result.setListResult(studentDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }
}
