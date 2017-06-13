package br.com.fiap.fuse.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="product")
public class Product {
	private String upc;
	private String name;
	private String description;
	private List<PricePerCompany> storesFound;
	
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PricePerCompany> getStoresFound() {
		return storesFound;
	}
	public void setStoresFound(List<PricePerCompany> storesFound) {
		this.storesFound = storesFound;
	}
	
	
}
