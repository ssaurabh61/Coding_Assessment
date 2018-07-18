package com.mybank.atm.service;

import com.mybank.atm.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@Import(AccountService.class)
@DataJpaTest
public class PinServiceTest {
    @TestConfiguration
    static class PinServiceImplTestContextConfiguration {
        @Bean
        public PinService pinService() {
            return new PinService();
        }
    }

    @Autowired
    private PinService pinService;

    // from src/test/resources/data.sql
    private Long testAccountNum = 987654321L;
    private Long invalidAccountNum = 99999L;

    @Test
    public void testValidPin() throws ServiceException {
        assertTrue("Pin validation failed", pinService.validatePin(testAccountNum, "4321"));
    }

    @Test
    public void testInvalidPin() throws ServiceException {
        assertFalse("Pin invalidation failed", pinService.validatePin(testAccountNum, "0000"));
    }

    @Test(expected = ServiceException.class)
    public void testEmptyPin() throws ServiceException {
        pinService.validatePin(33333L, "");
    }

    @Test(expected = ServiceException.class)
    public void testInvalidAccount() throws ServiceException {
        pinService.validatePin(invalidAccountNum, "0000");
    }
}
