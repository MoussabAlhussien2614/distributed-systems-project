package com.example.course_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CourseInstructer {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
}
