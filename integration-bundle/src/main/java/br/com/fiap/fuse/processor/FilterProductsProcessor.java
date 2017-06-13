package br.com.fiap.fuse.processor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.fuse.model.Product;

public class FilterProductsProcessor implements Processor {
	
	private static Logger log = LoggerFactory.getLogger(FilterProductsProcessor.class);

	@Override
	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
		List<Product> products = exchange.getIn().getMandatoryBody(List.class);

		String upc = (String) exchange.getIn().getHeader("upc");
		
		log.info("Filter by UPC = {}", upc);
		
		Stream<Product> filteredProducts = products.stream().filter(p -> p.getUpc().equals(upc));
		
		Optional<Product> opProduct = filteredProducts.findFirst();
		
		if (opProduct.isPresent()) {
			log.info("Found product...");
			exchange.getIn().setBody(opProduct.get());
		} else {
			throw new IllegalArgumentException(String.format("Product %s doesn't exists!", upc));
		}
	}

}
