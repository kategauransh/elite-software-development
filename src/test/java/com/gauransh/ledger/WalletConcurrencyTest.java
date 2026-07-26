package com.gauransh.ledger;

import com.gauransh.ledger.model.Wallet;
import com.gauransh.ledger.repository.WalletRepository;
import com.gauransh.ledger.service.WalletTransferFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WalletConcurrencyTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransferFacade transferFacade;

    @Test
    public void testConcurrentTransfers() throws InterruptedException {
        // Setup sender with 1000 balance, receiver with 0
        Wallet sender = walletRepository.save(new Wallet(new BigDecimal("1000.00")));
        Wallet receiver = walletRepository.save(new Wallet(new BigDecimal("0.00")));

        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    transferFacade.transferWithRetry(sender.getId(), receiver.getId(), new BigDecimal("10.00"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        Wallet updatedSender = walletRepository.findById(sender.getId()).orElseThrow();
        Wallet updatedReceiver = walletRepository.findById(receiver.getId()).orElseThrow();

        // 20 concurrent transactions of 10.00 each = 200.00 total transferred
        assertEquals(new BigDecimal("800.00"), updatedSender.getBalance());
        assertEquals(new BigDecimal("200.00"), updatedReceiver.getBalance());
    }
}
