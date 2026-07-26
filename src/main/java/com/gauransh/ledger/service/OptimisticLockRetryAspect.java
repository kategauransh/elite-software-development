package com.gauransh.ledger.service;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class WalletTransferFacade {
    private final WalletService walletService;
    private static final int MAX_RETRIES = 5;

    public WalletTransferFacade(WalletService walletService) {
        this.walletService = walletService;
    }

    public void transferWithRetry(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        int attempt = 0;
        while (true) {
            try {
                walletService.transfer(fromWalletId, toWalletId, amount);
                return;
            } catch (ObjectOptimisticLockingFailureException ex) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("Transaction failed after max retries due to concurrent updates", ex);
                }
                try {
                    // Backoff retry
                    Thread.sleep(20 + (int)(Math.random() * 50));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
    }
}
