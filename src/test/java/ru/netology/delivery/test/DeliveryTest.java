package ru.netology.delivery.test;

import com.codeborne.selenide.SelenideElement;

import static org.junit.jupiter.api.Assertions.*;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.UserInfo;
import ru.netology.delivery.util.ScreenShooterReportPortalExtension;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.delivery.util.LoggingUtils.loginfo;

@ExtendWith({ScreenShooterReportPortalExtension.class})
class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldGenerateTestDataUsingUtils() {
        UserInfo info = DataGenerator.Registration.generateUser("ru");
        String date = DataGenerator.generateDate(7);
        assertNotNull(info.getCity());
        assertNotNull(info.getName());
        assertNotNull(info.getPhone());
        assertNotNull(DataGenerator.generateDate(0));
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue(validUser.getCity());
        // для подробного логирования так нужно писать после каждой операции
        loginfo("В поле введен город"+validUser.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] .input__control").setValue(firstMeetingDate);
        form.$("[data-test-id=name] .input__control").setValue(validUser.getName());
        form.$("[data-test-id=phone] .input__control").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$(".button").find(exactText("Запланировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована на")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText(firstMeetingDate)).shouldBe(visible, Duration.ofSeconds(15));

        $("[data-test-id=success-notification] .icon-button__text").click();
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] .input__control").setValue(secondMeetingDate);
        form.$$(".button").find(exactText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button__content").click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована на")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText(secondMeetingDate)).shouldBe(visible, Duration.ofSeconds(15));
    }
}
