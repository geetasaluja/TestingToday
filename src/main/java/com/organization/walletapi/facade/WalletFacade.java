package com.organization.walletapi.facade;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.WalletDto;

/**
 *
 * @author Geeta Saluja
 */
@Component
public class WalletFacade {
	private ModelMapper modelMapper = new ModelMapper();
	public WalletDto convertToWalletDto(Wallet wallet) {
		WalletDto walletDto = modelMapper.map(wallet, WalletDto.class);
		return walletDto;
    }
	public Wallet convertToWallet(WalletDto walletDto) {
		Wallet wallet = modelMapper.map(walletDto, Wallet.class);
		return wallet;
    }
	public  List<WalletDto> domainToDtoWalletList(List<Wallet> wallets) {
		return wallets.stream().map(wallet -> modelMapper.map(wallet, WalletDto.class)).collect(Collectors.toList());
	}
}
