import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.api.DatabaseHelper;
import org.example.api.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class EmployeeApiTestBase {
    protected static final String BASE_URL = "https://innopolispython.onrender.com/api";
    protected static final String EMPLOYEE_PATH = "/employee";
    protected DatabaseHelper dbHelper;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        dbHelper = new DatabaseHelper();
        dbHelper.cleanupEmployeeData();
    }

    @AfterEach
    public void tearDown() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    protected RequestSpecification getRequestSpec() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    protected Employee createEmployeeViaApi(Employee employee) {
        Response response = getRequestSpec()
                .body(employee)
                .post(EMPLOYEE_PATH);

        return response.as(Employee.class);
    }

    protected Response getEmployeeResponse(Integer id) {
        return getRequestSpec()
                .get(EMPLOYEE_PATH + "/" + id);
    }
}
