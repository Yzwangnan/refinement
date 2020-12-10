package com.refinement.http;

import lombok.Data;

import java.util.List;

/**
 * Created by macro on 2020/6/19.
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private List<String> roles;
}
