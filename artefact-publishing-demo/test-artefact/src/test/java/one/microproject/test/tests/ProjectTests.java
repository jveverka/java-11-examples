package one.microproject.test.tests;

import org.junit.jupiter.api.Test;
import one.microproject.test.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTests {

    @Test
    public void testUtils() {
        assertEquals("hello world !", TestUtils.getMessage());
    }

}
