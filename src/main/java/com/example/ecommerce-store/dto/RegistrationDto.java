package com.example.safariwebstore008.dto;

import com.example.safariwebstore008.enums.Gender;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class RegistrationDto {

    private String firstName;
    private String lastName;
    private String email;
    @Size(min = 8,message = "The password character is less than 8")
    private String password;
    private Date dateOfBirth;
    private String gender;
}
