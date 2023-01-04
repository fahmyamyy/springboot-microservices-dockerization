package com.beyonder.lessonservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserCertsDTO {
    private Long id;
    private Long userId;
    private Long certId;

}
