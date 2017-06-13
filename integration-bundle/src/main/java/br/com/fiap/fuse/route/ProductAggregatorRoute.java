package br.com.fiap.fuse.route;

import java.io.File;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

import br.com.fiap.fuse.aggregator.ProductAggregator;
import br.com.fiap.fuse.aggregator.StoreProductsAggregator;
import br.com.fiap.fuse.model.Product;
import br.com.fiap.fuse.processor.FilterProductsProcessor;
import br.com.fiap.fuse.processor.PricePerCompanyProcessor;
import br.com.fiap.fuse.stores.model.StoreProduct;

public class ProductAggregatorRoute extends RouteBuilder {
	
	private static final String productsPath = "/home/mohashi/Projects/redhat/fiap/fiap-workshop/fiap-product-quotation/integration-bundle/src/data/products.json";

	@Override
	public void configure() throws Exception {
		restConfiguration()
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("api/v1")
				.component("netty4-http").host("0.0.0.0").port(8082)
			.bindingMode(RestBindingMode.json)
			.apiContextPath("/api-doc")
				.apiProperty("api.title", "Product API").apiProperty("api.version", "1")
				.apiProperty("cors", "true");
		
		rest("/product").id("rest-products")
			.get().description("List all products")
				.produces("application/json")
				.to("direct:list-products")
			.get("/{upc}").description("Retrieve product")
				.produces("application/json")
				.to("direct:product");
	
		from("direct:product").id("direct-product")
			.log(LoggingLevel.INFO, "FindByUPC Body = ${header.upc}")
			.setHeader("id", method(this, "getUUID"))
			.to("direct:load-products")
			.process(new FilterProductsProcessor())
			.to("direct:search-by-product")
			.log("Returning=${body}")
			.endRest();
		
		from("direct:list-products").id("direct-list-products")
		    .log(LoggingLevel.INFO, "FindAll")
		    .setHeader("id", method(this, "getUUID"))
		    .to("direct:load-products")
		    .split(body(), new ProductAggregator())
		    	.to("direct:search-by-product")
	    	.end()
	    	.log("Returning=${body}")
			.endRest();
		
		JacksonDataFormat jsonDF = new JacksonDataFormat(Product.class);
		jsonDF.setUseList(true);
		jsonDF.setUnmarshalType(Product.class);

		from("direct:load-products").id("direct-load-products")
			/*
			 * Need to substitute to transform in other to bug found.
			 *.pollEnrich("file:src/data?fileName=products.json&noop=true&idempotent=false&readLock=none", 100l)
			 */
			.transform().simple(productsPath, File.class)
			.log("Products file read...")
			.unmarshal(jsonDF)
			.log("Products file umarshalled...");
				
		JacksonDataFormat jsonStoreDF = new JacksonDataFormat(StoreProduct.class);
		jsonStoreDF.setUnmarshalType(StoreProduct.class);
    	
		from("direct:search-by-product").id("search-by-product")
			.log("Current Product = ${body}")
			.multicast()
				.enrich("direct:store-a-search", new StoreProductsAggregator())
				.enrich("direct:store-b-search", new StoreProductsAggregator())
			.end();
		
		from("direct:store-a-search").id("direct-store-a-search")
			.log("Searching for '${body.getUpc()}'")
			.setHeader(Exchange.HTTP_URI, constant("http://192.168.99.100.xip.io"))
			.setHeader(Exchange.HTTP_PATH, simple("api/v1/product/${body.getUpc()}"))
			.setHeader(Exchange.ACCEPT_CONTENT_TYPE, constant("application/json"))
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.removeHeader("upc")
			.transform().constant(null)
			.to("http4:store-a")
			.unmarshal(jsonStoreDF)
			.process(new PricePerCompanyProcessor("Store A"))
			.log(LoggingLevel.INFO, "FindOneByUPC from Store-A ${header.id} - ${body}").end();
		
		from("direct:store-b-search").id("direct-store-b-search")
			.log("Searching for '${body.getUpc()}'")
			.setHeader(Exchange.HTTP_URI, constant("http://localhost:8081"))
			.setHeader(Exchange.HTTP_PATH, simple("api/v1/product/${body.getUpc()}"))
			.setHeader(Exchange.ACCEPT_CONTENT_TYPE, constant("application/json"))
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.removeHeader("upc")
			.transform().constant(null)
			.to("http4:store-b")
			.unmarshal(jsonStoreDF)
			.process(new PricePerCompanyProcessor("Store B"))
			.log(LoggingLevel.INFO, "FindOneByUPC from Store-B ${header.id} - ${body}").end();
			
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
	
}
