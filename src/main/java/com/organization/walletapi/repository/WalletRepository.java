package com.organization.walletapi.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.organization.walletapi.domain.entities.Wallet;

/**
 *
 * @author Geeta Saluja
 */
public interface WalletRepository extends CrudRepository<Wallet, Long>{
	List<Wallet> findAll();
	Optional<Wallet> findByWalletId(Long walletId);
	@Query(nativeQuery = true, value = "select * from wallet where player_id = ?")
	Optional<Wallet> findByPlayerId(Long playerId);
}
