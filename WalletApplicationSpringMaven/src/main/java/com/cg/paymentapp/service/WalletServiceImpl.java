
package com.cg.paymentapp.service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.paymentapp.beans.Customer;
import com.cg.paymentapp.beans.Wallet;
import com.cg.paymentapp.exception.InsufficientBalanceException;
import com.cg.paymentapp.exception.InvalidInputException;
import com.cg.paymentapp.repo.WalletRepo;


@Service
public class WalletServiceImpl implements WalletService{
@Autowired
private WalletRepo repo;
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	public WalletServiceImpl() {
	}
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		if(amount.intValue()>0 && name!=null ){
			Wallet wallet= new Wallet(amount);
		Customer customer = new Customer(name,mobileNo, wallet);
		if(repo.save(customer)){
			return customer;
		}
		else
			throw new InvalidInputException("Mobile Number is already registered...");
		}
		else
			throw new InvalidInputException("Negative Amount entered...");
				
		}

	public Customer showBalance(String mobileNo) {
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		Customer giver = repo.findOne(sourceMobileNo);
		Customer taker = repo.findOne(targetMobileNo);
		System.out.println("Giver :"+ giver);
		System.out.println("Taker: "+ taker);
		if(giver!=null && taker!=null){
		Wallet sourceWallet = giver.getWallet();
		Wallet targetWallet = taker.getWallet();
		
		if(sourceWallet != null && targetWallet != null) {
			if (sourceWallet.getBalance().compareTo(amount)>=0){
				System.out.println("transfering amount....");
			sourceWallet.setBalance(sourceWallet.getBalance().subtract(amount));
			targetWallet.setBalance(targetWallet.getBalance().add(amount));
			
			
			giver.setWallet(sourceWallet);
			taker.setWallet(targetWallet);
			
			repo.save(giver);
			repo.save(taker);
			System.out.println("amount transfered..");
			return giver;
			}
			else
				throw new InsufficientBalanceException("Balance is not sufficient for transfer");
			}
		else
			throw new InvalidInputException("Invalid amount in account");
		}
		else
			throw new InvalidInputException("Invalid input ");
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		if(amount.intValue()>0){
		Customer customer = repo.findOne(mobileNo);
		if(customer!=null && customer.getWallet()!=null){
			Wallet wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().add(amount));
			customer.setWallet(wallet);
			repo.save(customer);
			return customer;
		}
		else
			throw new InvalidInputException("Invalid details for Deposit");
		}
		else
			throw new InvalidInputException("Negative amount for deposit");
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		// TODO Auto-generated method stub
		Customer customer = repo.findOne(mobileNo);
		if(customer!=null && customer.getWallet()!=null){
			if(amount.intValue()<= customer.getWallet().getBalance().intValue()){
			Wallet wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().subtract(amount));
			customer.setWallet(wallet);
			repo.save(customer);
			return customer;
		}
			else
				throw new InsufficientBalanceException("Low balance for withdraw");
		}
		else
			throw new InvalidInputException("Invalid Account Details for Withdrawl");
	}

	@Override
	public List<Customer> getList() {
		// TODO Auto-generated method stub
		return repo.getList();
	}
}
