package br.com.fiap.fuse.stores.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="product")
@XmlAccessorType(XmlAccessType.FIELD)
public class StoreProduct {
	private String id;
	private String upc;
	private BigInteger quantity;
	private BigDecimal price;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public BigInteger getQuantity() {
		return quantity;
	}
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
