package ru.netology.delivery.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor

public class UserInfo {
    private String city;
    private String name;
    private String phone;


}
