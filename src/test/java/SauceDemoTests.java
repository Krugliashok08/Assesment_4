import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.example.pages.*;
import org.example.utils.TestData;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selenide.*;

@Epic("SauceDemo E-commerce Tests")
@Feature("Авторизация и оформление заказов")
@DisplayName("Тесты для сайта Saucedemo")
public class SauceDemoTests {

    @BeforeEach
    @Step("Настройка браузера")
    public void setUp() {
        // Настройка Selenide
        com.codeborne.selenide.Configuration.browser = "firefox";
        com.codeborne.selenide.Configuration.timeout = 10000;
        com.codeborne.selenide.Configuration.pageLoadTimeout = 20000;
        com.codeborne.selenide.Configuration.browserSize = "1920x1080";

        // Включение логирования для Allure
        com.codeborne.selenide.Configuration.reportsFolder = "target/screenshots";
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Успешная авторизация стандартного пользователя")
    @Description("Проверка успешного входа с валидными credentials стандартного пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("auth")
    @Tag("smoke")
    public void successfulLoginTest() {
        LoginPage loginPage = new LoginPage().open();

        assertTrue(loginPage.isOnLoginPage(), "Страница логина должна отображаться");

        ProductsPage productsPage = loginPage.loginAsStandardUser();

        assertTrue(productsPage.isOnProductsPage(),
                "Пользователь должен быть перенаправлен на страницу товаров после успешной авторизации");
    }

    @Test
    @DisplayName("Авторизация заблокированного пользователя")
    @Description("Проверка отображения ошибки при входе заблокированного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auth")
    public void lockedOutUserLoginTest() {
        LoginPage loginPage = new LoginPage().open();

        assertTrue(loginPage.isOnLoginPage(), "Страница логина должна отображаться");

        loginPage.loginWithLockedUser();

        assertTrue(loginPage.isErrorMessageDisplayed(),
                "Должно отображаться сообщение об ошибке");
        assertEquals(TestData.LOCKED_USER_ERROR, loginPage.getErrorMessage(),
                "Текст ошибки должен соответствовать ожидаемому");
    }

    @Nested
    @DisplayName("E2E сценарии оформления заказа")
    @Tag("e2e")
    @Tag("regression")
    class E2ETests {

        @Test
        @DisplayName("E2E тест для standard_user: полный цикл покупки")
        @Description("Полный E2E сценарий от авторизации до завершения заказа для standard_user")
        @Severity(SeverityLevel.CRITICAL)
        public void e2eTestStandardUser() {
            performE2eTest(TestData.STANDARD_USER);
        }

        @Test
        @DisplayName("E2E тест для performance_glitch_user: полный цикл покупки")
        @Description("Полный E2E сценарий от авторизации до завершения заказа для performance_glitch_user")
        @Severity(SeverityLevel.CRITICAL)
        public void e2eTestPerformanceUser() {
            performE2eTest(TestData.PERFORMANCE_GLITCH_USER);
        }

        @Step("Выполнение E2E теста для пользователя: {username}")
        private void performE2eTest(String username) {
            // 1. Авторизация
            LoginPage loginPage = new LoginPage().open();
            ProductsPage productsPage = loginWithUser(loginPage, username);

            // 2. Добавление товаров в корзину
            addProductsToCart(productsPage);

            // 3. Переход в корзину и проверка товаров
            CartPage cartPage = productsPage.goToCart();
            verifyCartItems(cartPage);

            // 4. Переход к оформлению заказа
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();

            // 5. Заполнение формы
            CheckoutOverviewPage overviewPage = checkoutPage.fillCheckoutInformation();

            // 6. Проверка суммы заказа
            verifyOrderTotal(overviewPage);

            // 7. Завершение заказа
            overviewPage.finishOrder();
            verifyOrderCompletion(overviewPage);
        }

        @Step("Авторизация пользователя: {username}")
        private ProductsPage loginWithUser(LoginPage loginPage, String username) {
            ProductsPage productsPage;
            if (TestData.STANDARD_USER.equals(username)) {
                productsPage = loginPage.loginAsStandardUser();
            } else {
                productsPage = loginPage.loginAsPerformanceUser();
            }
            assertTrue(productsPage.isOnProductsPage(),
                    "После авторизации должна отображаться страница товаров");
            return productsPage;
        }

        @Step("Добавление товаров в корзину")
        private void addProductsToCart(ProductsPage productsPage) {
            productsPage.addProductsToCart(
                    TestData.BACKPACK,
                    TestData.BOLT_TSHIRT,
                    TestData.ONESIE
            );

            assertEquals(3, productsPage.getCartItemsCount(),
                    "В корзине должно быть 3 товара");
        }

        @Step("Проверка товаров в корзине")
        private void verifyCartItems(CartPage cartPage) {
            assertTrue(cartPage.isOnCartPage(), "Должна открыться страница корзины");
            assertEquals(3, cartPage.getItemsCount(),
                    "В корзине должно отображаться 3 товара");

            // Проверяем что все добавленные товары присутствуют в корзине
            assertTrue(cartPage.isProductInCart(TestData.BACKPACK),
                    "Товар " + TestData.BACKPACK + " должен быть в корзине");
            assertTrue(cartPage.isProductInCart(TestData.BOLT_TSHIRT),
                    "Товар " + TestData.BOLT_TSHIRT + " должен быть в корзине");
            assertTrue(cartPage.isProductInCart(TestData.ONESIE),
                    "Товар " + TestData.ONESIE + " должен быть в корзине");
        }

        @Step("Проверка общей суммы заказа")
        private void verifyOrderTotal(CheckoutOverviewPage overviewPage) {
            assertTrue(overviewPage.isOnOverviewPage(),
                    "Должна открыться страница обзора заказа");
            String totalAmount = overviewPage.getTotalAmount();
            assertEquals(TestData.EXPECTED_TOTAL, totalAmount,
                    String.format("Общая сумма заказа должна быть %s", TestData.EXPECTED_TOTAL));
        }

        @Step("Проверка завершения заказа")
        private void verifyOrderCompletion(CheckoutOverviewPage overviewPage) {
            assertTrue(overviewPage.isOrderComplete(),
                    "Должно отображаться сообщение о завершении заказа");
            assertEquals(TestData.ORDER_COMPLETE_MESSAGE, overviewPage.getCompleteMessage(),
                    "Сообщение о завершении заказа должно соответствовать ожидаемому");
        }
    }
}