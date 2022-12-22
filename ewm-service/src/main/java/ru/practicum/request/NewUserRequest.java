package ru.practicum.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class NewUserRequest {
    @Email
    private String email;

    @NotNull
    private String name;
}
