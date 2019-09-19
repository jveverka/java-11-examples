package itx.avro.tests;

import itx.avro.AvroUtils;
import itx.avro.Employee;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AvroSerializationDeserializationTests {

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        Employee employeeIn = Employee.newBuilder()
                .setFirstName("Juraj")
                .setLastName("Veverka")
                .setAge(17)
                .setPhoneNumber("+1 123 456 789")
                .build();

        byte[] data = AvroUtils.serializeEmployee(employeeIn);

        Assert.assertNotNull(data);
        Assert.assertTrue(data.length > 0);

        Employee employeeOut = AvroUtils.deserializeEmployee(data);

        Assert.assertNotNull(employeeOut);
        Assert.assertEquals(employeeIn.getFirstName(), employeeOut.getFirstName());
        Assert.assertEquals(employeeIn.getLastName(), employeeOut.getLastName());
        Assert.assertEquals(employeeIn.getPhoneNumber(), employeeOut.getPhoneNumber());
        Assert.assertTrue(employeeIn.getAge() == employeeOut.getAge());
        Assert.assertEquals(employeeIn, employeeOut);
    }

}
