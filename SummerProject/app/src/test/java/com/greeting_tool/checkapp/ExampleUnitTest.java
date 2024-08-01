package com.greeting_tool.checkapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.tool.greeting_tool.common.utils.FormatCheckerUtil;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     * test Format checker
     */
    @Test
    public void testFormatChecker(){

        // postcode
        assertFalse(FormatCheckerUtil.checkPostcode("BBBBBB"));
        assertFalse(FormatCheckerUtil.checkPostcode("BBB BBB"));
        assertTrue(FormatCheckerUtil.checkPostcode("BS1 3NX"));

        // password
        assertFalse(FormatCheckerUtil.checkPassword("mypassword"));
        assertFalse(FormatCheckerUtil.checkPassword("it"));
        assertFalse(FormatCheckerUtil.checkPassword("Mypassword!"));
        assertTrue(FormatCheckerUtil.checkPassword("Mypassword123!"));

        // email
        assertTrue(FormatCheckerUtil.checkEmail("1111@outlook.com"));
        assertFalse(FormatCheckerUtil.checkEmail("1111@outlook"));
        assertTrue(FormatCheckerUtil.checkEmail("1111@o.com"));
        assertFalse(FormatCheckerUtil.checkEmail("@outlook.com"));

    }
}