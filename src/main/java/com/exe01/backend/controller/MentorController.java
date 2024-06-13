package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Mentor Controller")
public class MentorController {

    @Autowired
    private IMentorService mentorService;

    @Operation(summary = "Get all mentor", description = "API get all mentor")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTOR)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all mentor with page: {}, limit: {}", page, limit);
        return mentorService.getAll(page, limit);
    }

    @Operation(summary = "Get all mentor", description = "API get all mentor")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTOR_WITH_ALL_INFORMATION)
    public PagingModel getAllWithAllInformation(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all mentor with page: {}, limit: {}", page, limit);
        return mentorService.getMentorsWithAllInformation(page, limit);
    }

    @Operation(summary = "Get all mentor with status active", description = "API get all mentor with status active")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTOR_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active mentor with page: {}, limit: {}", page, limit);
        return mentorService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get mentor by id", description = "API get mentor by id")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTOR_BY_ID + "{id}")
    public MentorDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting mentor with id: {}", id);
            return mentorService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get mentor by mentor profile id", description = "API get mentor by mentor profile id")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTOR_BY_MENTOR_PROFILE_ID + "{id}")
    public MentorsResponse findByMentorProfileId(@PathVariable("id") UUID id) throws BaseException {
        log.info("Getting mentor with mentor profile id: {}", id);
        return mentorService.getMentorByMentorProfileId(id);
    }

    @Operation(summary = "Get mentors by company id", description = "API get mentors by company id")
    @GetMapping(value = ConstAPI.MentorAPI.GET_MENTORS_BY_COMPANY_ID + "{id}")
    public List<MentorsResponse> findByCompanyId(@PathVariable("id") UUID id) throws BaseException {
        log.info("Getting mentors with company id: {}", id);
        return mentorService.getMentorsByCompanyId(id);
    }

    @Operation(summary = "Create mentor", description = "API create new mentor")
    @PostMapping(value = ConstAPI.MentorAPI.CREATE_MENTOR)
    public CreateMentorResponse create(@RequestBody CreateMentorRequest request) throws BaseException {
        log.info("Creating new mentor with request: {}", request);
        return mentorService.create(request);
    }

    @Operation(summary = "Update mentor", description = "API update mentor")
    @PutMapping(value = ConstAPI.MentorAPI.UPDATE_MENTOR + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateMentorRequest request) throws BaseException {
        log.info("Updating mentor with id: {}, request: {}", id, request);
        return mentorService.update(id, request);
    }

    @Operation(summary = "Delete mentor", description = "API delete mentor")
    @DeleteMapping(value = ConstAPI.MentorAPI.CHANGE_STATUS_MENTOR + "{id}")
    public Boolean changStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting mentor with id: {}", id);
        return mentorService.changeStatus(id);
    }

}
