package com.example.userservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginateUserDTO implements Serializable {
    private List<UserDTO> userDTOList;
    private Long totalOfItems;
    private Integer currentPage;
    private Integer totalOfPages;

}
