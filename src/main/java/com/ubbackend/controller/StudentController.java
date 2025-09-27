package com.ubbackend.controller;

import com.ubbackend.DTO.*;
import com.ubbackend.services.StudentService;

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
        name = "Student controller",
        description = "Student controller where all the endpoints are located"
)
@RestController
@RequestMapping("/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    @Operation(
            summary = "fetch all students",
            description = "This method return all students in DB",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of students",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "array",
                                            implementation = StudentRecursionDTO.class
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
    public ResponseEntity<List<StudentRecursionDTO>> getStudents() {
        System.out.println("-> at students ##### ");
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());
    }

    @Operation(
            summary = "fetch a student",
            description = "Method to get a student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A student",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = StudentRecursionDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Student not found",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentRecursionDTO> getStudent(
            @Parameter(
                    name = "id",
                    description = "The student id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            format = "Long",
                            description = "Param ID student that needs to be fetched",
                            allowableValues = {"1", "2", "3"}
                    )
            ) @PathVariable Long id) throws Exception {
        System.out.println("-> at student ##### ");
        Optional<StudentRecursionDTO> studentRecursionExisting = studentService.getStudent(id);
        return studentRecursionExisting
                .map(studentRecursionDTO -> ResponseEntity.status(HttpStatus.OK).body(studentRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @Operation(
            summary = "Create a student",
            description = "Method to create student, view all attributes in StudentDTO",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "this method receive a class with all attributes for create student, view all attributes in StudentDTO",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The student successfully created, and the response is the course where student was saved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CourseRecursionDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The course does´t exist, The student could´t be created",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Dni is has already been used, internal server error",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/students")
    public ResponseEntity<CourseRecursionDTO> createStudent(@RequestBody StudentDTO studentDTO) throws Exception {
        Optional<CourseRecursionDTO> courseExisting = studentService.createStudent(studentDTO);
        return courseExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.CREATED).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(
            summary = "Delete a student",
            description = "Method to remove a student whit the id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Student successfully deleted",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Info message that student was deleted"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user id doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/students/{idStudent}/course/{idCourse}")
    public ResponseEntity<String> deleteStudent(
            @Parameter(
                    name = "idStudent",
                    description = "The student id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID student that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
    ) @PathVariable Long idStudent,
            @Parameter(
                    name = "idCourse",
                    description = "The cours id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID course that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            ) @PathVariable Long idCourse) {
        if(!studentService.deleteStudent(idStudent, idCourse)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully");
    }

    @Operation(
            summary = "Update student",
            description = "Method to update student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
                            this method receive a class with all attributes to update student.
                            \s
                             IMPORTANT\s
                            \s
                            if you don't want to update any attribute of type text, just send (null).
                            \s
                            In the case of Dni and Semester, if you don't want to update send 0, Otherwise send num > 0.
                            \s""",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentUpdateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The user successfully update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = StudentRecursionDTO.class,
                                            description = "The student updated"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The student doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/students/{id}")
    public ResponseEntity<StudentRecursionDTO> updateStudent(
            @Parameter(
                    name = "id",
                    description = "The student id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID student that needs to be updated",
                            allowableValues = {"1", "2", "3"}
                    )
    ) @PathVariable Long id, @RequestBody StudentUpdateDTO studentUpdateDTO) throws Exception {
        Optional<StudentRecursionDTO> studentRecursionExisting = studentService.updateStudent(id, studentUpdateDTO);
        return studentRecursionExisting
        .map(student -> ResponseEntity.status(HttpStatus.OK).body(student))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "Add grade to student",
            description = "Method to add grade to the student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "This method receive a class whit all attributes to add grade to the student, view all attributes in StudentGradeDTO",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    description = "Class whit all attributes to add a new grade",
                                    implementation = StudentGradeDTO.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Student whit all grades and whit the latest grade added",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            description = "Student update whit the new grade",
                                            implementation = StudentRecursionDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Student or course not found",
                            content =  @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
                    )
            }
    )
    @PostMapping("students/notes")
    public ResponseEntity<StudentRecursionDTO> addGradeToStudent(@RequestBody StudentGradeDTO studentGradeDTO) throws Exception {

        Optional<StudentRecursionDTO> studentRecursionDTO = studentService.addGradeToStudent(studentGradeDTO);
        return studentRecursionDTO
                .map(recursionDTO -> ResponseEntity.status(HttpStatus.OK).body(recursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
