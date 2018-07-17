package com.cg.paymentapp.repo;

import java.util.ArrayList;
import java.util.List;

import com.cg.paymentapp.beans.Customer;

public interface WalletRepo {

	public boolean save(Customer customer);
	public Customer findOne(String mobileNo);
	public List<Customer> getList();
	public boolean merge(Customer customer);
}
