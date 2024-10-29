package com.iftm.startexample.dto;

import java.io.Serializable;

import com.iftm.startexample.models.Address;
import com.iftm.startexample.models.User;

import lombok.Data;

@Data
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private int age;
    private Address address;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId().toString();
        this.name = user.getName().toString();
        this.age = user.getAge();
        this.address = user.getAddress();
    }
}
