package com.mybank.atm.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExceptionTest {

    @Test
    public void testServiceException() {
        final String code = "T111";
        final String message = "A service exception Message";

        ServiceException se = new ServiceException(code, message);
        assertEquals("Message is not correct", message, se.getMessage());
        assertEquals("Code is not correct", code, se.getCode());
    }

    @Test
    public void testApiException() {
        final String code = "T112";
        final String message = "An Api exception Message";

        ApiException e = new ApiException(new ServiceException(code, message));

        assertEquals("Message is not correct", message, e.getMessage());
        assertEquals("Code is not correct", code, e.getCode());
    }
}
