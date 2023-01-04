package com.beyonder.lessonservice.controller;

import com.beyonder.lessonservice.dto.ResponseDTO;
import com.beyonder.lessonservice.dto.UserLessonDTO;
import com.beyonder.lessonservice.service.UserLessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userLessons")
@Slf4j
@RequiredArgsConstructor
public class UserLessonController {
    private final UserLessonService userLessonService;
    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserLessonDTO>>> getAllUserLesson(){
        log.info("Hit controller getting all user lesson");
        List<UserLessonDTO> userLessonDTOList = userLessonService.getAllUserLesson();
        return new ResponseEntity<ResponseDTO<List<UserLessonDTO>>>(ResponseDTO
                .<List<UserLessonDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get all user lesson")
                .data(userLessonDTOList).build(), HttpStatus.OK);
    }
    //ResponseDTO.<UserLessonDTO>builder().httpStatus(HttpStatus.OK).message("success get user with id "+id).data(userDTO).build()
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserLessonDTO>> getUserLessonById(@PathVariable Long id) {
        log.info("Hit controller getting user lesson by id");
        UserLessonDTO userLessonDTO = userLessonService.getUserLessonById(id);
        return new ResponseEntity<ResponseDTO<UserLessonDTO>>(ResponseDTO.<UserLessonDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get user with id "+id)
                .data(userLessonDTO).build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addUserLesson(@RequestBody UserLessonDTO userLessonDTO) {
        log.info("Hit controller for add user lesson");
        userLessonService.addUserLesson(userLessonDTO);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("success add user")
                .data(userLessonDTO).build(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserLessonDTO>> updateUserLesson(@RequestBody UserLessonDTO userLessonDTO, @PathVariable Long id) {
        log.info("hit controller for update user lesson by id");
        userLessonService.updateUserLesson(userLessonDTO, id);
        return new ResponseEntity<ResponseDTO<UserLessonDTO>>(ResponseDTO.<UserLessonDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success update user")
                .data(userLessonDTO)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteUserLesson(@PathVariable Long id) {
        log.info("hit controller for deleting user lesson by id");
        userLessonService.deleteUserLesson(id);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .message("success delete user")
                .build(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/{userId}/lesson/{lessonId}")
    public Boolean isUserLessonExist(@PathVariable Long userId, @PathVariable Long lessonId){
        log.info("hit controller for checking userLesson exist");
        return userLessonService.existsByUserIdLessonId(userId,lessonId);
    }
}
