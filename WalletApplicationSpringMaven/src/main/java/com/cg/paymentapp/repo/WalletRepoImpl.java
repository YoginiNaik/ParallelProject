package com.cg.paymentapp.repo;
import java.util.List;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cg.paymentapp.beans.Customer;
import com.cg.paymentapp.exception.InvalidInputException;
@Repository
public class WalletRepoImpl implements WalletRepo{

	
	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public boolean save(Customer customer) {
		Customer cus= findOne(customer.getMobileNo());
		if(cus==null) {
	entityManager.persist(customer);
	entityManager.flush();
	System.out.println("entity Persisted...");
		return true;
		}
		else if(customer.equals(cus)){
			cus.setWallet(customer.getWallet());
			entityManager.merge(cus);
			entityManager.flush();
			System.out.println("entity mergred...");
			return true;
		}
		else 
			throw new InvalidInputException("Account is already existing" );
		
	}

	public Customer findOne(String mobileNo) {
		Customer customer=entityManager.find(Customer.class, mobileNo);
		return customer;
	}

	@Override
	public List<Customer> getList() {
		TypedQuery<Customer> query=entityManager.createQuery("select cust from Customer cust ", Customer.class);
		List<Customer> list=query.getResultList();
		return list;
	}
	@Override
	public boolean merge(Customer customer) {
		// TODO Auto-gene
			Customer cust= findOne(customer.getMobileNo());
			cust.setWallet(customer.getWallet());
			entityManager.merge(cust);
			entityManager.flush();
			return true;
	}
}
