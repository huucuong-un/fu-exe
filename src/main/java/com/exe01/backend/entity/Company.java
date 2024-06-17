package com.exe01.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company_tbl")
public class Company extends BaseEntity {

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Company type must be less than or equal to 100 characters")
    @Column(name = "company_type", nullable = false)
    private String companyType;

    @Column(name = "company_size")
    private Integer companySize;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Size(max = 100, message = "Country must be less than or equal to 100 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "working_time")
    private String workingTime;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "overtime_policy")
    private String overtimePolicy;

    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "company")
    private List<Mentor> mentors = new ArrayList<>();

}
