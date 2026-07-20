package com.gauransh.saas;

import com.gauransh.saas.tenant.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TenantIsolationTest {

    @Test
    public void testThreadLocalIsolation() throws Exception {
        // Assert ThreadLocal segregates values between executing threads
        Thread thread1 = new Thread(() -> {
            TenantContext.setTenantId("TenantA");
            try { Thread.sleep(50); } catch (Exception ignored) {}
            assertEquals("TenantA", TenantContext.getTenantId());
        });

        Thread thread2 = new Thread(() -> {
            TenantContext.setTenantId("TenantB");
            assertEquals("TenantB", TenantContext.getTenantId());
        });

        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
    }
}
