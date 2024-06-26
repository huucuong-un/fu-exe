package com.exe01.backend.dto;

import com.exe01.backend.dto.response.mentorProfile.FindMentorProfileByIdResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfileDTO extends BaseDTO implements Serializable {

    private String linkedinUrl;

    private String requirement;

    private String description;

    private String shortDescription;

    private String profilePicture;

    private String status;

    private FindMentorProfileByIdResponse mentorDTO;

}
