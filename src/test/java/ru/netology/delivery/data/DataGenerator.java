package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


@UtilityClass
public class DataGenerator {

    public static String generateDate(int shift) {

        String date = LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        return date;
    }

    public static String generateCity() {

        City cities = new City();
        Random rand = new Random();
        String city = cities.getCities()[rand.nextInt(cities.getCities().length)];
        return city;
    }

    public static String generateName(String locale) {

        Faker faker = new Faker(new Locale(locale));
        String name = faker.name().fullName();
        return name;
    }

    public static String generatePhone(String locale) {

        Faker faker = new Faker(new Locale(locale));
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {

            UserInfo user = new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
            return user;
        }
    }
}
