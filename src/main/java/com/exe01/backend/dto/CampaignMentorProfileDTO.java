package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignMentorProfileDTO extends BaseDTO implements Serializable {

    private CampaignDTO campaign;

    private MentorProfileDTO mentorProfile;

}
