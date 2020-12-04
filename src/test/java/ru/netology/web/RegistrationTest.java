package ru.netology.web;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class RegistrationTest {
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    @Test
    void shouldRegisterSuccessful() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 13000);
    }

    @Test
    void shouldRegisterAllFieldsEmpty() {
        open("http://localhost:9999");
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongCity() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Chicago");
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongDate() {
        open("http://localhost:9999");
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
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='name'] input").setValue("Oleg Ivanov");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterWrongPhone() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+7920000000");
        $("[data-test-id='agreement']").click();
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно")).waitUntil(visible, 3000);
    }

    @Test
    void shouldRegisterDoNotPushRadioButton() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='name'] input").setValue("Василий Петров");
        $("[data-test-id='phone'] input").setValue("+79200000000");
        $$("[type='button']").find(exactText("Забронировать")).click();
        $(withText("Я соглашаюсь с условиями обработки")).waitUntil(visible, 3000);
    }
}

