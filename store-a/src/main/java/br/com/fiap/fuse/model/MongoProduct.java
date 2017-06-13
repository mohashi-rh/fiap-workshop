package br.com.fiap.fuse.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MongoProduct {
	private String _id;
	private String upc;
	private BigDecimal price;
	private BigInteger quantity;

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
