package com.beyonder.lessonservice.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LessonDTO implements Serializable {
    private Long id;
    private String name;

}
