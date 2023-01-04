package com.beyonder.lessonservice.service;


import com.beyonder.lessonservice.dto.UserLessonDTO;

import java.util.List;

public interface UserLessonService {
    List<UserLessonDTO> getAllUserLesson();
    UserLessonDTO getUserLessonById(Long id);
    void addUserLesson(UserLessonDTO userLessonDTO);
    void updateUserLesson(UserLessonDTO userLessonDTO, Long id);
    void deleteUserLesson(Long id);
    Boolean existsByUserIdLessonId(Long userId, Long lessonId);
}
