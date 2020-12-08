package ru.netology.web;

import com.codeborne.selenide.conditions.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class RegistrationTest {
    LocalDate date = LocalDate.now();
    int dayOfMonth = date.plusDays(3).getDayOfMonth();
    int month = date.getMonthValue();
    int year = date.getYear();
    String dayOfMonthStr = String.valueOf(dayOfMonth);
    String yearStr = String.valueOf(year);
    String monthStr = String.valueOf(month);
    String validDate = dayOfMonthStr + monthStr + yearStr;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldRegisterSuccessful() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(validDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText(dayOfMonthStr + "." + monthStr + "." + yearStr)).waitUntil(visible, 13000);
    }

    @Test
    void shouldChooseCityFromDropDownList() {
        $("[data-test-id='city'] input").setValue("Мо");
        $$(By.className("menu-item__control")).find(exactText("Смоленск")).click();
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        $("[data-test-id='date'] input").setValue(validDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText(dayOfMonthStr + "." + monthStr + "." + yearStr)).waitUntil(visible, 13000);
    }

    @Test
    void shouldDateFromCalendarApp() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $(By.className("icon_name_calendar")).click();
        $("[data-step='1']").doubleClick();
        $$(By.className("calendar__day")).find(exactText("16")).click();
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("16.02.2021")).waitUntil(visible, 13000);
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
        $("[data-test-id='date'] input").setValue(validDate);
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
        $("[data-test-id='date'] input").setValue(validDate);
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
        $("[data-test-id='date'] input").setValue(validDate);
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
        $("[data-test-id='date'] input").setValue(validDate);
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $$("[type='button']").find(exactText("Забронировать")).click();
        $$("[data-test-id='agreement'].input_invalid .checkbox__text")
                .find(new Text("Я соглашаюсь с условиями обработки и использования моих персональных данных"))
                .waitUntil(visible, 3000);
    }
}
