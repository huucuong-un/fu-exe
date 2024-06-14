package com.exe01.backend.dto;

import com.exe01.backend.dto.response.mentorProfile.FindMentorProfileByIdResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorProfileDTO extends BaseDTO implements Serializable {

    private String linkedinUrl;

    private String requirement;

    private String description;

    private String shortDescription;

    private String profilePicture;

    private String status;

    private MentorDTO mentorDTO;
}
