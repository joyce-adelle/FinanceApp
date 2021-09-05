package com.trove.project.services.implementations;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.exceptions.InsufficientFundsException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.models.entities.Shares;
import com.trove.project.models.entities.Stock;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.PortfolioRepository;
import com.trove.project.repositories.SharesRepository;
import com.trove.project.repositories.StockRepository;
import com.trove.project.repositories.UserRepository;
import com.trove.project.security.SecurityUtils;
import com.trove.project.services.SharesService;

@Service
@Transactional
public class SharesServiceImpl implements SharesService {

	@Autowired
	SharesRepository sharesRepository;

	@Autowired
	StockRepository stockRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PortfolioRepository portfolioRepository;

	@Override
	public @NotNull Portfolio buyShares(@NotNull @Min(1) Long stockId, @Positive @NotNull Double quantity) {

		Stock stock = stockRepository.findById(stockId)
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		User user = SecurityUtils.getCurrentUserId().flatMap(userRepository::findById)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		BigDecimal cost = stock.getPricePerShare().multiply(new BigDecimal(quantity));
		BigDecimal wallet = user.getWallet().subtract(cost);
		if (wallet.signum() >= 0) {
			Optional<Shares> share = this.sharesRepository.findOneByIdUserAndIdStock(user, stock);
			user.setWallet(wallet);

			if (share.isPresent()) {
				Shares shares = share.get();
				shares.setQuantity(shares.getQuantity() + quantity);
				this.sharesRepository.save(shares);
			}

			this.sharesRepository.save(new Shares(user, stock, quantity));
			return this.userRepository.save(user).getPortfolio();
		} else
			throw new InsufficientFundsException("not enough money in wallet to buy shares");

	}

	@Override
	public @NotNull Portfolio sellShares(@NotNull @Min(1) Long stockId, @Positive @NotNull Double quantity) {
		Stock stock = stockRepository.findById(stockId)
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		User user = SecurityUtils.getCurrentUserId().flatMap(userRepository::findById)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Shares share = this.sharesRepository.findOneByIdUserAndIdStock(user, stock)
				.orElseThrow(() -> new ResourceNotFoundException("Shares not found"));

		if (quantity > share.getQuantity())
			throw new InsufficientFundsException("not enough shares to sell");

		BigDecimal profit = stock.getPricePerShare().multiply(new BigDecimal(quantity));
		user.setWallet(user.getWallet().add(profit));
		Double newQuantity = share.getQuantity() - quantity;

		if (newQuantity > 0.0) {
			share.setQuantity(newQuantity);
			sharesRepository.save(share);
		} else
			sharesRepository.delete(share);

		return this.userRepository.save(user).getPortfolio();

	}

}
