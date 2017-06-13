package br.com.fiap.fuse.model;

import java.math.BigDecimal;

public class PricePerCompany {
	private BigDecimal price;
	private String company;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
}
