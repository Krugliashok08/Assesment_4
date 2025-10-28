package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import org.example.utils.TestData;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage {

    private final SelenideElement firstNameField = $("#first-name");
    private final SelenideElement lastNameField = $("#last-name");
    private final SelenideElement zipCodeField = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");
    private final SelenideElement checkoutTitle = $(".title");

    @Step("Заполнить информацию для checkout валидными данными")
    public CheckoutOverviewPage fillCheckoutInformation() {
        return fillCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ZIP_CODE);
    }

    @Step("Заполнить информацию для checkout: Имя: {firstName}, Фамилия: {lastName}, ZIP: {zipCode}")
    public CheckoutOverviewPage fillCheckoutInformation(String firstName, String lastName, String zipCode) {
        firstNameField.setValue(firstName);
        lastNameField.setValue(lastName);
        zipCodeField.setValue(zipCode);
        continueButton.click();
        return new CheckoutOverviewPage();
    }

    @Step("Проверить, что находимся на странице checkout")
    public boolean isOnCheckoutPage() {
        return checkoutTitle.isDisplayed() && checkoutTitle.getText().equals("Checkout: Your Information");
    }
}