package com.example.admin.weatherapp;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private MainActivity mainActivity=new MainActivity();

    /**
     * Test method for setTempCelcius(String temperature) method within MainActivity
     */
    @Test
    public void testSetTemperatureC() throws Exception {
        String temp = mainActivity.setTempCelsius("38.5");

        assertNotNull(temp);
        assertEquals("38 \u2103", temp);
        assertTrue(mainActivity.setTempCelsius("1").equals("1 \u2103"));
    }

    /**
     * Test method for setTempFahrenheit(String temperature) method within MainActivity
     */
    @Test
    public void testSetTemperatureF() throws Exception {
        String temp = mainActivity.setTempFahrenheit("38.5");

        assertNotNull(temp);
        assertNotEquals("38 \u2103", temp);
        assertEquals("38 \u2109", temp);
        assertTrue(mainActivity.setTempFahrenheit("1").equals("1 \u2109"));
    }

    /**
     * Test method for setTempFahrenheit(String temperature) method within MainActivity
     */

    @Test
    public void testSetCityRegion() throws Exception {
        String temp = mainActivity.setCity("Sydney","NSW");

        assertNotNull(temp);
        assertNotEquals("Sydney, NSW", temp);
        assertEquals("Sydney, NSW", temp);
        assertTrue(mainActivity.setCity("Sydney","NSW").equals("Sydney,NSW"));
    }
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}