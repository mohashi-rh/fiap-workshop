package br.com.fiap.fuse.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.fuse.model.Product;

public class ProductAggregator implements AggregationStrategy {
	private Logger log = LoggerFactory.getLogger(ProductAggregator.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		try {
			// put order together in old exchange by adding the order from new
			// exchange

			if (oldExchange == null) {
				// the first time we aggregate we only have the new exchange
				
				Product product = newExchange.getIn().getMandatoryBody(Product.class);
				List<Product> products = new ArrayList<>();
				products.add(product);
				newExchange.getIn().setBody(products);
				
				return newExchange;
			}
			List<Product> products = oldExchange.getIn().getMandatoryBody(List.class);
			Product newProduct = newExchange.getIn().getMandatoryBody(Product.class);

			log.debug("Aggregate old list of products: {} with new {}", products.size(), newProduct);

			products.add(newProduct);
		} catch (InvalidPayloadException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		// return old as this is the one that has all the orders gathered until
		// now
		return oldExchange;
	}

}
