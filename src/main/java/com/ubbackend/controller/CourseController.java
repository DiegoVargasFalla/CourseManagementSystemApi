package com.ubbackend.controller;

import com.ubbackend.DTO.*;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Course controller",
        description = "Course controller where all the endpoints are located"
)
@RestController
@RequestMapping("/v1")
public class CourseController {
    public final CourseService courseService;

    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
    }

    @Operation(
            summary = "fetch all Courses",
            description = "This method return all courses in DB",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of courses",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "array",
                                            implementation = CourseRecursionDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @GetMapping("/courses")
    public ResponseEntity<List<CourseRecursionDTO>> getCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourses());
    }

    @Operation(
            summary = "fetch a course",
            description = "Method to get a course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A course",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CourseRecursionDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "course not found",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseRecursionDTO> getCourseById(
            @Parameter(
                    name = "id",
                    description = "The course id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            format = "Long",
                            description = "Param ID course that needs to be fetched",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id) {
        Optional<CourseRecursionDTO> courseRecursionDTO = courseService.getCourse(id);
        return courseRecursionDTO
                .map(recursionDTO -> ResponseEntity.status(HttpStatus.OK).body(recursionDTO)).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "Create a course",
            description = "Method to create course, view all attributes in CourseDTO",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "this method receive a class with all attributes for create course, view all attributes in CourseDTO",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The course successfully created, and the response is the course recent add course",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CourseEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Course schedule id wrong",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/courses")
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDTO courseDTO) throws Exception {
        Optional<CourseEntity> courseRecursionDTOExisting = courseService.createCourse(courseDTO);
        return courseRecursionDTOExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.CREATED).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(
            summary = "Delete course",
            description = "Method to remove a course whit the id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course successfully deleted",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Info message that course was deleted"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The course id doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(
            @Parameter(
                    name = "id",
                    description = "The course id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID course that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id) throws Exception {
        if(courseService.deleteCourse(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Course successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course not found");
    }

    @Operation(
            summary = "Update course",
            description = "Method to update course",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "this method receive a class with all attributes to update course.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CourseUpdateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The course successfully update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CourseRecursionDTO.class,
                                            description = "The course updated"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/courses")
    public ResponseEntity<CourseRecursionDTO> updateCourse(@RequestBody CourseUpdateDTO courseUpdateDTO) throws Exception {
        Optional<CourseEntity> courseUpdateExisting = courseService.updateCourse(courseUpdateDTO);

        if(courseUpdateExisting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
        courseRecursionDTO.setId(courseUpdateExisting.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(courseRecursionDTO);
    }

    @Operation(
            summary = "Add a student to one course",
            description = "Method to add a student to course",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "this method receive a class with all attributes to add a student to one course.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NewStudentDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The student successfully added",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CourseRecursionDTO.class,
                                            description = "The student added"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user or course doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "The student already exist in this course",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/courses/students")
    public ResponseEntity<CourseRecursionDTO> addStudentToCourse(@RequestBody NewStudentDTO newStudentDTO) throws Exception {
        Optional<CourseRecursionDTO> courseRecursionDTOExisting = courseService.newStudent(newStudentDTO);

        return courseRecursionDTOExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.OK).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "delete a student from one course",
            description = "Method to delete a student from one course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The student successfully deleted from course",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CourseRecursionDTO.class,
                                            description = "The student added"

                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user or course doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/courses/{courseId}/students/{studentDni}")
    public ResponseEntity<?> deleteStudentFromCourse(
            @Parameter(
                    name = "courseId",
                    description = "The course id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID course that the student needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            ) @PathVariable Long courseId,
            @Parameter(
                    name = "studentDni",
                    description = "The student id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID student that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            ) @PathVariable Long studentDni) {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setCourseId(courseId);
        newStudentDTO.setDni(studentDni);

        Optional<CourseRecursionDTO> courseRecursionDTOExisting = courseService.deleteStudentFromCourse(newStudentDTO);
        return courseRecursionDTOExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.OK).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
