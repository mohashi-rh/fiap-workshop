package br.com.fiap.fuse.model;

import java.math.BigDecimal;
import java.math.BigInteger;

//@Entity
//@NamedQueries({
//	@NamedQuery(name="findProdByUPC", query="from br.com.fiap.fuse.entity.Product p where p.upc=:upc"),
//	@NamedQuery(name="findAll", query="from br.com.fiap.fuse.entity.Product p")
//})
//@Table(name="product")
public class Product {
//	@Id
	private String id;
	private String upc;
	private BigDecimal price;
	private BigInteger quantity;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigInteger getQuantity() {
		return quantity;
	}
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

}
