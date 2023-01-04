package com.beyonder.lessonservice.repository;

import com.beyonder.lessonservice.entity.UserLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLessonRepository extends JpaRepository<UserLesson, Long> {
    @Query(
            value = "SELECT * FROM user_lessons u WHERE u.user_id = :userId AND u.lesson_id = :lessonId",
            nativeQuery = true)
    Optional<UserLesson> existsByUserIdLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
}
