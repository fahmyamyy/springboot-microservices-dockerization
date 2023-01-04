package com.beyonder.lessonservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginateLessonDTO implements Serializable {
    private List<LessonDTO> lessonDTOList;
    private Long totalOfItems;
    private Integer currentPage;
    private Integer totalOfPages;

}
