package com.gauransh.ledger.service;

import com.gauransh.ledger.model.Wallet;
import com.gauransh.ledger.model.LedgerEntry;
import com.gauransh.ledger.repository.WalletRepository;
import com.gauransh.ledger.repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public WalletService(WalletRepository walletRepository, LedgerEntryRepository ledgerEntryRepository) {
        this.walletRepository = walletRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @Transactional
    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Wallet sender = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Sender wallet not found"));
        Wallet receiver = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver wallet not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        walletRepository.save(sender);
        walletRepository.save(receiver);

        ledgerEntryRepository.save(new LedgerEntry(fromWalletId, amount, "DEBIT"));
        ledgerEntryRepository.save(new LedgerEntry(toWalletId, amount, "CREDIT"));
    }
}
