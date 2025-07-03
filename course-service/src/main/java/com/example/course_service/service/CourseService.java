package com.example.course_service.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.course_service.dto.request.CreateCourseRequest;
import com.example.course_service.dto.request.UpdateCourseRequest;
import com.example.course_service.dto.response.CourseResponse;
import com.example.course_service.model.Course;
import com.example.course_service.repository.CourseRepository;

@Service
public class CourseService {
   private CourseRepository courseRepository;


   public CourseService(CourseRepository courseRepository){
      this.courseRepository = courseRepository;
   }
   public List<CourseResponse> list(){
      List<Course> courses = courseRepository.findAll();
      
      List<CourseResponse> result = courses.stream()
         .map((Course course) -> CourseResponse.builder()
            .id(course.getId())
            .name(course.getName())
            .instructerId(course.getInstructerId())
            .tutionFee(course.getTuitionFee())
            .isApproved(course.getIsApproved())
            .build())
         .collect(Collectors.toList());
      return result;
   }   

    public CourseResponse retrieve(Long id){
      Course course = courseRepository.findById(id)
         .orElseThrow(() -> new RuntimeException("course not found"));
      
      return  CourseResponse.builder()
            .id(course.getId())
            .name(course.getName())
            .instructerId(course.getInstructerId())
            .tutionFee(course.getTuitionFee())
            .isApproved(course.getIsApproved())
            .build();
   }   


   public CourseResponse create(CreateCourseRequest request){
      Course course = Course.builder()
         .name( request.getName())
         .instructerId((long) 1)
         .tuitionFee(request.getTuitionFee())
         .isApproved(false)
         .build();
      courseRepository.save(course);
      return CourseResponse.builder()
         .id(course.getId())
         .name(course.getName())
         .instructerId(course.getInstructerId())
         .tutionFee(course.getTuitionFee())
         .isApproved(course.getIsApproved())
         .build();
   } 

   public CourseResponse update(Long id ,UpdateCourseRequest request) {
      Course course = courseRepository.findById(id)
         .orElseThrow(() -> new RuntimeException("Id not found."));

      course.setName(Objects.requireNonNullElse(request.getName(), course.getName()));
      course.setTuitionFee(Optional.ofNullable(request.getTuitionFee()).orElse(course.getTuitionFee()));
      course.setIsApproved(Optional.ofNullable(request.getIsApproved()).orElse(course.getIsApproved()));
      courseRepository.save(course);

      return CourseResponse.builder()
         .id(course.getId())
         .name(course.getName())
         .instructerId(course.getInstructerId())
         .tutionFee(course.getTuitionFee())
         .isApproved(course.getIsApproved())
         .build();
   } 

    public Void delete(Long id){
      Course course = courseRepository.findById(id)
         .orElseThrow(() -> new RuntimeException("Id not found."));

      courseRepository.delete(course);
      return null;
   } 
}


