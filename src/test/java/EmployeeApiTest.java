import io.restassured.response.Response;
import org.example.api.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeApiTest extends EmployeeApiTestBase {


    @Test
    void testCreateEmployeePositive() {
        Employee newEmployee = new Employee("John", "Brown", "Admin", "NY");

        Employee createdEmployee = createEmployeeViaApi(newEmployee);

        assertNotNull(createdEmployee.getId());
        assertEquals(newEmployee.getName(), createdEmployee.getName());
        assertEquals(newEmployee.getSurname(), createdEmployee.getSurname());
        assertEquals(newEmployee.getPosition(), createdEmployee.getPosition());
        assertEquals(newEmployee.getCity(), createdEmployee.getCity());

        assertTrue(dbHelper.employeeExistsInDb(createdEmployee.getId()));
    }

    @Test
    void testGetEmployeeByIdPositive() {
        Integer employeeId = dbHelper.createEmployeeInDb("John", "Brown", "Admin", "NY");

        Response response = getEmployeeResponse(2);
        Employee employee = response.as(Employee.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(employeeId, employee.getId());
        assertEquals("John", employee.getName());
        assertEquals("Brown", employee.getSurname());
    }

    @Test
    void testUpdateEmployeePositive() {
        Integer employeeId = dbHelper.createEmployeeInDb("John", "Brown", "Admin", "NY");
        Employee updatedData = new Employee("Jack", "White", "QA", "LA");

        Response response = getRequestSpec()
                .body(updatedData)
                .put(EMPLOYEE_PATH + "/" + employeeId);

        Employee updatedEmployee = response.as(Employee.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(employeeId, updatedEmployee.getId());
        assertEquals("Разработчик", updatedEmployee.getPosition());
    }

    @Test
    void testDeleteEmployeePositive() {
        Integer employeeId = dbHelper.createEmployeeInDb("Jack", "White", "QA", "LA");

        Response response = getRequestSpec()
                .delete(EMPLOYEE_PATH + "/" + 2);

        assertEquals(200, response.getStatusCode());
        assertFalse(dbHelper.employeeExistsInDb(employeeId));
    }

    @Test
    void testGetNonExistentEmployee() {
        Response response = getEmployeeResponse(999999);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testDeleteNonExistentEmployee() {
        Response response = getRequestSpec()
                .delete(EMPLOYEE_PATH + "/999999");

        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testUpdateNonExistentEmployee() {
        Employee updatedData = new Employee("NONE", "NONE", "NONE", "NONE");

        Response response = getRequestSpec()
                .body(updatedData)
                .put(EMPLOYEE_PATH + "/999999");

        assertEquals(404, response.getStatusCode());

    }
}