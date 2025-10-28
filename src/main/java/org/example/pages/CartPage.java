package org.example.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    private final ElementsCollection cartItems = $$(".cart_item");
    private final SelenideElement checkoutButton = $("#checkout");
    private final SelenideElement cartTitle = $(".title");

    @Step("Получить количество товаров в корзине")
    public int getItemsCount() {
        return cartItems.size();
    }

    @Step("Нажать кнопку Checkout")
    public CheckoutPage proceedToCheckout() {
        checkoutButton.click();
        return new CheckoutPage();
    }

    @Step("Проверить, что находимся на странице корзины")
    public boolean isOnCartPage() {
        return cartTitle.isDisplayed() && cartTitle.getText().equals("Your Cart");
    }

    @Step("Проверить наличие товара в корзине")
    public boolean isProductInCart(String productName) {
        return cartItems.findBy(com.codeborne.selenide.Condition.text(productName)).isDisplayed();
    }
}