package com.beyonder.lessonservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LessonWithReview<T> {
    private Long id;
    private String name;
//    private Integer star;
    private T review;

}
