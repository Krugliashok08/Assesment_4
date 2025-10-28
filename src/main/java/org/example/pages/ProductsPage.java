package org.example.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.utils.TestData;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductsPage {

    private final SelenideElement shoppingCart = $("#shopping_cart_container");
    private final ElementsCollection productItems = $$(".inventory_item");
    private final SelenideElement cartBadge = $(".shopping_cart_badge");
    private final SelenideElement productsTitle = $(".title");

    @Step("Добавить товар '{productName}' в корзину")
    public ProductsPage addProductToCart(String productName) {
        findProductByName(productName).$("button").click();
        return this;
    }

    @Step("Добавить товары в корзину: {productNames}")
    public ProductsPage addProductsToCart(String... productNames) {
        for (String productName : productNames) {
            addProductToCart(productName);
        }
        return this;
    }

    @Step("Перейти в корзину")
    public CartPage goToCart() {
        shoppingCart.click();
        return new CartPage();
    }

    @Step("Получить количество товаров в корзине")
    public int getCartItemsCount() {
        return cartBadge.exists() ? Integer.parseInt(cartBadge.getText()) : 0;
    }

    @Step("Проверить что находимся на странице товаров")
    public boolean isOnProductsPage() {
        return productsTitle.isDisplayed() && productsTitle.getText().equals("Products");
    }

    @Step("Найти товар по имени: {productName}")
    private SelenideElement findProductByName(String productName) {
        return productItems.findBy(com.codeborne.selenide.Condition.text(productName));
    }
}