package com.beyonder.lessonservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserLessonDTO {
    private Long id;

    private Long userId;

    private Long lessonId;

}
