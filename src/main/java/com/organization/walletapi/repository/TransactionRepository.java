package com.organization.walletapi.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.domain.entities.Transaction;

/**
 *
 * @author Geeta Saluja
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long>{
	Optional<Transaction> findByCallerSupplyTransId(String callerSupplyTransId);
	Set<Transaction> findByWallet(Wallet wallet);
}
