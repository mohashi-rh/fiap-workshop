package br.com.fiap.fuse.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.fiap.fuse.model.PricePerCompany;
import br.com.fiap.fuse.stores.model.StoreProduct;

public class PricePerCompanyProcessor implements Processor {
	private String company;
	
	public PricePerCompanyProcessor(String company) {
		this.company = company;
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		StoreProduct storeProduct = exchange.getIn().getBody(StoreProduct.class);
		
		PricePerCompany pricePerCompany = new PricePerCompany();
		pricePerCompany.setCompany(company);
		pricePerCompany.setPrice(storeProduct.getPrice());
		
		exchange.getIn().setBody(pricePerCompany);
	}

}
