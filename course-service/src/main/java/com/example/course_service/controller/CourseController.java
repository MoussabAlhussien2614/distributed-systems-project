package com.example.course_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.course_service.dto.request.CreateCourseRequest;
import com.example.course_service.dto.request.UpdateCourseRequest;
import com.example.course_service.dto.response.CourseResponse;
import com.example.course_service.model.CourseInstructer;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.service.CourseService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private CourseService courseService;
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping("/status")
    public String status() {
        return "Course service is up and running";
    }

    @GetMapping()
    public List<CourseResponse> list() {
        var response = this.courseService.list();
        return response;
    }
    
    @GetMapping("/{id}")
    public CourseResponse retrieve(@PathVariable Long id) {
        var response = this.courseService.retrieve(id);
        return response;
    }

    @GetMapping("/{id}/instructer")
    public CourseInstructer retrieveIntructer(@PathVariable Long id, @RequestHeader("Authorization") String auth) {
        System.out.println(auth);
        var response = courseService.retrieveInstructer(id, auth);
        return response;
    }
    
    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CreateCourseRequest request) {
        var response = this.courseService.create(request);

        return ResponseEntity.ok(response);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable Long id, @RequestBody UpdateCourseRequest request) {
        var response = this.courseService.update(id, request);

        return ResponseEntity.ok(response);
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.courseService.delete(id);

        return ResponseEntity.ok(null);
    }
    
}
