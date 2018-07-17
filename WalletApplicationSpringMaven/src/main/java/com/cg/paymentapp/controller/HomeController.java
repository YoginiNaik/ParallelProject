package com.cg.paymentapp.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.paymentapp.beans.Customer;
import com.cg.paymentapp.exception.InvalidInputException;
import com.cg.paymentapp.service.WalletService;

@RestController
public class HomeController {

	@Autowired  
	private WalletService service;
	public WalletService getService() {
		return service;
	}
	public void setService(WalletService service) {
		this.service = service;
	}
	@RequestMapping(value="/payment/{name}",method=RequestMethod.GET)
	public String sayHello(@PathVariable("name") String name) {
		return name+ " welcome to REST world ";
	}
	
	@RequestMapping(value="/payment/createAccountAction",
			method=RequestMethod.POST,produces="application/json")
	public Customer createAccount(@RequestParam("custName")String name,
			@RequestParam("mobileNo")String mobileNo,
			@RequestParam("amount") int amount){
		try {
			BigDecimal bg=new BigDecimal(amount);
			Customer customer=service.createAccount(name, mobileNo, bg);
			return customer;
		} catch (Exception e) {
			e.printStackTrace();	
			return null;
				
			}
	}
		@RequestMapping(value="/payment/getBalanceAction/{mobileno}"
				,produces="application/json")
	public Customer getBalanceDetails(@PathVariable("mobileno")String mob){
		try {
			Customer customer= service.showBalance(mob);
			System.out.println(customer);
			return customer;
			
		} catch (InvalidInputException e) {
			e.printStackTrace();
					return null;
		}
	}

	@RequestMapping(value="/payment/transfterFundAction",
			method=RequestMethod.POST,produces="application/json")
	public Customer transferFund(@RequestParam("sourceMobNo") String source,
			         @RequestParam("targetMobNo") String target,
			         @RequestParam("amount") double amount) {
		BigDecimal amt= new BigDecimal(amount);
		try {
			Customer customer=service.fundTransfer(source, target, amt);
			return customer;
		} catch (Exception e) {
			e.printStackTrace();
	return null;	}
	}

	@RequestMapping(value="/payment/addMoneyAction",
			method=RequestMethod.POST,produces="application/json")
	public Customer addMoneyAction(@RequestParam("mobileNo") String mobNo,
			@RequestParam("amount") double amount){
			BigDecimal amt= new BigDecimal(amount);
		try {
			Customer customer=service.depositAmount(mobNo, amt);
	return customer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			}
		}
	@RequestMapping(value="/payment/sendMoneyAction",
			method=RequestMethod.POST,produces="application/json")
	public Customer sendMoneyToBankAccountAction(@RequestParam("mobileNo") String mobNo,
			@RequestParam("amount") double amount,Model model){
			BigDecimal amt= new BigDecimal(amount);
		try {
			Customer customer=service.withdrawAmount(mobNo, amt);
		return customer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
	}
		
	}

}
