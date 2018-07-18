package com.mybank.atm.entity.json;

import com.mybank.atm.entity.db.BankNote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class WithdrawalResourceTest {
    @Test
    public void testCreate() {
        WithdrawalResource withdrawalResource = new WithdrawalResource();
        AccountResource accountResource = new AccountResource(BigDecimal.valueOf(200L), BigDecimal.valueOf(500L));
        withdrawalResource.setAccountResource(accountResource);
        Map<BankNote, Integer> map = new HashMap<>();
        map.put(BankNote.FIFTY, 1);
        withdrawalResource.setAmount(BigDecimal.valueOf(100L));
        withdrawalResource.setCashMap(new CashMapResource(map));

        assertEquals("Account resource balance incorrect", BigDecimal.valueOf(200L), withdrawalResource.getAccountResource().getBalance());
        assertEquals("Account resource withdrawal incorrect", BigDecimal.valueOf(500L), withdrawalResource.getAccountResource().getMaxWithdrawal());
        assertEquals("Balance incorrect", BigDecimal.valueOf(100L), withdrawalResource.getAmount());
        assertEquals("Map incorrect", (long) map.get(BankNote.FIFTY), withdrawalResource.getCashMap().getFifty());
    }
}
