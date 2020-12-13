package ru.netology.web;

import com.codeborne.selenide.conditions.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class RegistrationTest {
    String meetingDate = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    String meetingDateCalendarApp = LocalDate.now().plusDays(5).plusMonths(2)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    StringBuilder stringBuilder = new StringBuilder(meetingDate);
    StringBuilder meetingDateCut = stringBuilder.replace(2, 10, "");

    StringBuilder stringBuilderTill10 = new StringBuilder(meetingDate);
    StringBuilder meetingDateCutTill10 = stringBuilderTill10.replace(2, 10, "").replace(0, 1, "");

    LocalDate date = LocalDate.now().plusDays(5);
    int dayOfMonth = date.getDayOfMonth();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldRegisterSuccessful() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText(meetingDate)).waitUntil(visible, 13000);
    }

    @Test
    void shouldChooseCityFromDropDownList() {
        $("[data-test-id='city'] input").setValue("Мо");
        $$(By.className("menu-item__control")).find(exactText("Смоленск")).click();
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText(meetingDate)).waitUntil(visible, 13000);
    }

    @Test
    void shouldChooseDateFromCalendarApp() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $(By.className("icon_name_calendar")).click();
        $("[data-step='1']").doubleClick();

        if (dayOfMonth <= 9) {
            $$(By.className("calendar__day")).find(exactText(meetingDateCutTill10.toString())).click();
        } else {
            $$(By.className("calendar__day")).find(exactText(meetingDateCut.toString())).click();
        }

        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText(meetingDateCalendarApp)).waitUntil(visible, 13000);
    }

    @Test
    void shouldRegisterAllFieldsEmpty() {
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongCity() {
        $("[data-test-id='city'] input").setValue("Chicago");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongDate() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue("10.12.2019");
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongName() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Oleg Ivanov");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongPhone() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+7920000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterDoNotPushRadioButton() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $$("[type='button']").find(exactText("Забронировать")).click();
        $$("[data-test-id='agreement'].input_invalid .checkbox__text")
                .find(new Text("Я соглашаюсь с условиями обработки и использования моих персональных данных"))
                .waitUntil(visible, 3000);
    }
}
