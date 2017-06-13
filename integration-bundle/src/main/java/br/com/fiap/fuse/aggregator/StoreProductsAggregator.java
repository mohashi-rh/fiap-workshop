package br.com.fiap.fuse.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.fuse.model.PricePerCompany;
import br.com.fiap.fuse.model.Product;

public class StoreProductsAggregator implements AggregationStrategy {
	private Logger log = LoggerFactory.getLogger(StoreProductsAggregator.class);

	@Override
	@SuppressWarnings("unchecked")
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		try {
			// put order together in old exchange by adding the order from new
			// exchange

			if (oldExchange == null) {
				// the first time we aggregate we only have the new exchange,
				
				return newExchange;
			}
			Product product = oldExchange.getIn().getMandatoryBody(Product.class);
			PricePerCompany pricePerCompany = newExchange.getIn().getMandatoryBody(PricePerCompany.class);

			log.debug("Aggregate old products: {} with {}", product, pricePerCompany);

			List<PricePerCompany> pricePerCompanyList = product.getStoresFound();
			
			if (pricePerCompanyList == null) {
				pricePerCompanyList = new ArrayList<>();
				product.setStoresFound(pricePerCompanyList);
			}
			
			pricePerCompanyList.add(pricePerCompany);
			
		} catch (InvalidPayloadException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		
		// return old as this is the one that has all the orders gathered until
		// now
		return oldExchange;
	}
}