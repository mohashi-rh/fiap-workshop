package br.com.fiap.fuse.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.fiap.fuse.model.MongoProduct;
import br.com.fiap.fuse.model.Product;

public class MongoToProductProcessor implements Processor {

	private final Boolean single;
	
	public MongoToProductProcessor(Boolean single) {
		this.single = single;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
		if (single) {
			MongoProduct mongoProduct = exchange.getIn().getBody(MongoProduct.class);
			if (mongoProduct != null) {
				Product product = toProduct(mongoProduct);
				exchange.getIn().setBody(product);
			}
		} else {
			List<MongoProduct> mongoProductList = exchange.getIn().getBody(List.class);
			if (mongoProductList != null) {
				List<Product> productList = new ArrayList<Product>();
				for (MongoProduct mongoProduct : mongoProductList) {
					Product product = toProduct(mongoProduct);
					productList.add(product);
				}
				exchange.getIn().setBody(productList);
			}
		}
	}

	private Product toProduct(MongoProduct mongoProduct) {
		Product product = new Product();
		product.setId(mongoProduct.get_id());
		product.setPrice(mongoProduct.getPrice());
		product.setQuantity(mongoProduct.getQuantity());
		product.setUpc(mongoProduct.getUpc());
		return product;
	}

}
