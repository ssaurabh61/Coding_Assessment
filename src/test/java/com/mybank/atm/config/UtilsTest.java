package com.mybank.atm.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class UtilsTest {
    @Test
    public void maskingTest() {
        assertEquals("Maksing failed", "****", Utils.maskString("1234"));
    }
}
