package com.beyonder.lessonservice.controller;


import com.beyonder.lessonservice.dto.LessonDTO;
import com.beyonder.lessonservice.dto.LessonWithReview;
import com.beyonder.lessonservice.dto.PaginateLessonDTO;
import com.beyonder.lessonservice.dto.ResponseDTO;
import com.beyonder.lessonservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Slf4j
public class LessonController {
    private final RestTemplate restTemplate;

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<LessonDTO>>> getAllLesson() {
        log.info("Hit controller for getting all lessons");
        List<LessonDTO> lessonDTOList = lessonService.getAllLesson();
        return new ResponseEntity<ResponseDTO<List<LessonDTO>>>(ResponseDTO
                .<List<LessonDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get all lessons")
                .data(lessonDTOList).build(), HttpStatus.OK);
    }

    //    @GetMapping("/pagination")
//    public ResponseEntity<ResponseDTO<PaginateLessonDTO>> getAllUsersWithPagination(Pageable pageable){
//        PaginateLessonDTO paginateUserDTO = lessonService.getAllLessonWithPagination(pageable);
//        return new ResponseEntity<ResponseDTO<PaginateLessonDTO>>(ResponseDTO
//                .<PaginateLessonDTO>builder()
//                .httpStatus(HttpStatus.OK)
//                .message("success get all lessons")
//                .data(paginateUserDTO).build(), HttpStatus.OK);
//    }
    @GetMapping("/pagination")
    public ResponseEntity<ResponseDTO<PaginateLessonDTO>> getAllUsersWithPagination(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit
    ) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, limit);

        PaginateLessonDTO paginateUserDTO = lessonService.getAllLessonWithPagination(pageable);
        return new ResponseEntity<ResponseDTO<PaginateLessonDTO>>(ResponseDTO
                .<PaginateLessonDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get all lessons")
                .data(paginateUserDTO).build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<LessonDTO>> getLessonById(@PathVariable Long id) {
        log.info("Hit controller for getting lesson by id");
        LessonDTO userDTO = lessonService.getLessonById(id);
        return new ResponseEntity<ResponseDTO<LessonDTO>>(ResponseDTO.<LessonDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get lesson with id " + id)
                .data(userDTO).build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addLesson(@RequestBody LessonDTO lessonDTO) {
        log.info("Hit controller for adding lesson");
        lessonService.addLesson(lessonDTO);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("success add lesson")
                .data(lessonDTO).build(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<LessonDTO>> updateLesson(@RequestBody LessonDTO userDTO, @PathVariable Long id) {
        log.info("Hit controller for updating lesson by id");
        lessonService.updateLesson(userDTO, id);
        return new ResponseEntity<ResponseDTO<LessonDTO>>(ResponseDTO.<LessonDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success update lesson with id " + id)
                .data(userDTO)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteLesson(@PathVariable Long id) {
        log.info("Hit controller for deleting lesson by id");
        lessonService.deleteLesson(id);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .message("success delete lesson with id " + id)
                .build(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/reviews")
    public ResponseDTO getAllLessonReviews() {
        log.info("Hit controller for getting all lesson review");
        return restTemplate.getForObject("http://localhost:8083/api/v1/lesson_reviews/", ResponseDTO.class);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ResponseDTO> getLessonReviewsByLessonId(@PathVariable Long id) {
        log.info("Hit controller for getting lesson review by lesson id");
        ResponseDTO response = restTemplate.getForObject("http://localhost:8083/api/v1/lesson_reviews/lesson/" + id, ResponseDTO.class);

        LessonDTO lessonDTO = lessonService.getLessonById(id);

        LessonWithReview lessonWithReview = LessonWithReview.builder()
                .id(lessonDTO.getId())
                .name(lessonDTO.getName())
                .review(response.getData())
                .build();
        return new ResponseEntity<>(ResponseDTO.builder()
                .httpStatus(HttpStatus.OK)
                .message("success get lesson with id " + id + " with reviews")
                .data(lessonWithReview)
                .build(), HttpStatus.OK);
    }

//    @GetMapping("/{id}/finish")
//    public ResponseEntity<ResponseDTO<UserCertsDTO>> finishLesson(@RequestHeader(name = "Authorization") String tokenBearer, @PathVariable Long id){
//        log.info("hit controller for finish lesson");
//        UserCertsDTO userCertsDTO = lessonService.buildUserCertsDTO(tokenBearer, id);
//        rabbitTemplate.convertAndSend("new_lesson_exchange", "new_lesson_routing_key", userCertsDTO);
//        return new ResponseEntity<ResponseDTO<UserCertsDTO>>(ResponseDTO.<UserCertsDTO>builder().httpStatus(HttpStatus.CREATED).message("success create user certs").data(userCertsDTO).build(), HttpStatus.CREATED);
//    }

    @GetMapping("/{id}/take")
    public ResponseEntity<ResponseDTO> takeLesson(@RequestHeader(name = "Authorization") String tokenBearer, @PathVariable Long id) {
        lessonService.takeLesson(tokenBearer, id);
        return new ResponseEntity<>(ResponseDTO.builder().httpStatus(HttpStatus.CREATED).message("lesson taken").build(), HttpStatus.CREATED);
    }

//    @GetMapping("/{lessonId}/isUserLessonExist/")
//    public ResponseEntity<Boolean> existsUserLesson(@RequestHeader(name = "Authorization") String tokenBearer, @PathVariable Long lessonId) {
//        Boolean isExist = lessonService.isUserTakeLesson(lessonId, tokenBearer);
//        return new ResponseEntity<>(isExist,HttpStatus.OK);
//    }
}
