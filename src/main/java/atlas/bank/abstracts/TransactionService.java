package atlas.bank.abstracts;

import atlas.bank.DTOs.TransactionRequestDTO;
import atlas.bank.entities.Transaction;
import jakarta.transaction.Transactional;

public interface TransactionService {
    @Transactional
    Transaction deposit(TransactionRequestDTO dto);

    @Transactional
    Transaction withdraw(TransactionRequestDTO dto);

    @Transactional
    Transaction internalTransfer(TransactionRequestDTO dto);

    @Transactional
    Transaction externalTransfer(TransactionRequestDTO dto);
}
