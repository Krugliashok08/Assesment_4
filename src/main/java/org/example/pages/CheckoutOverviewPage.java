package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutOverviewPage {

    private final SelenideElement totalLabel = $(".summary_total_label");
    private final SelenideElement finishButton = $("#finish");
    private final SelenideElement completeHeader = $(".complete-header");
    private final SelenideElement overviewTitle = $(".title");

    @Step("Получить общую сумму заказа")
    public String getTotalAmount() {
        return totalLabel.getText().replace("Total: ", "");
    }

    @Step("Нажать кнопку Finish")
    public CheckoutOverviewPage finishOrder() {
        finishButton.click();
        return this;
    }

    @Step("Получить сообщение о завершении заказа")
    public String getCompleteMessage() {
        return completeHeader.getText();
    }

    @Step("Проверить, что находимся на странице обзора заказа")
    public boolean isOnOverviewPage() {
        return overviewTitle.isDisplayed() && overviewTitle.getText().equals("Checkout: Overview");
    }

    @Step("Проверить, что заказ завершен")
    public boolean isOrderComplete() {
        return completeHeader.isDisplayed();
    }
}