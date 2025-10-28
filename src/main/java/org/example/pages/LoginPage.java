package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.utils.TestData;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {

    private final SelenideElement usernameField = $("#user-name");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement loginButton = $("#login-button");
    private final SelenideElement errorMessage = $("[data-test='error']");

    @Step("Открыть страницу логина")
    public LoginPage open() {
        com.codeborne.selenide.Selenide.open(TestData.BASE_URL);
        return this;
    }

    @Step("Логин как standard_user")
    public ProductsPage loginAsStandardUser() {
        return login(TestData.STANDARD_USER, TestData.PASSWORD);
    }

    @Step("Логин как performance_glitch_user")
    public ProductsPage loginAsPerformanceUser() {
        return login(TestData.PERFORMANCE_GLITCH_USER, TestData.PASSWORD);
    }

    @Step("Логин с username: {username}")
    public ProductsPage login(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        loginButton.click();
        return page(ProductsPage.class);
    }

    @Step("Логин заблокированным пользователем")
    public LoginPage loginWithLockedUser() {
        usernameField.setValue(TestData.LOCKED_OUT_USER);
        passwordField.setValue(TestData.PASSWORD);
        loginButton.click();
        return this;
    }

    @Step("Получить текст ошибки")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Проверить отображение ошибки")
    public boolean isErrorMessageDisplayed() {
        return errorMessage.isDisplayed();
    }

    @Step("Проверить, что находимся на странице логина")
    public boolean isOnLoginPage() {
        return loginButton.isDisplayed();
    }
}

