package com.beyonder.lessonservice.service;


import com.beyonder.lessonservice.dto.LessonDTO;
import com.beyonder.lessonservice.dto.PaginateLessonDTO;
import com.beyonder.lessonservice.dto.UserCertsDTO;
import com.beyonder.lessonservice.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonService {
    List<LessonDTO> getAllLesson();
    PaginateLessonDTO getAllLessonWithPagination(Pageable pageable);
    LessonDTO getLessonById(Long id);
    void addLesson(LessonDTO lessonDTO);
    void updateLesson(LessonDTO lessonDTO, Long id);
    void deleteLesson(Long id);
    void takeLesson(String token, Long id);
    UserCertsDTO buildUserCertsDTO(String token, Long id);
    Boolean isUserTakeLesson(Long userId, Long lessonId);
}
