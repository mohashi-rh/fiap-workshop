package br.com.fiap.fuse.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;


public class ProductPriceRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		restConfiguration()
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("api/v1")
				.component("netty4-http").host("0.0.0.0").port(8081)
			.bindingMode(RestBindingMode.json)
			.apiContextPath("/api-doc")
				.apiProperty("api.title", "Product API").apiProperty("api.version", "1")
				.apiProperty("cors", "true");
		
		rest("/product").id("store-b-rest-product")
			.get().description("List all products")
				.produces("application/json")
				.to("direct:listProducts")
			.get("/{upc}").description("Retrieve product")
				.produces("application/json")
				.to("direct:product");
		
		from("direct:product").id("store-b-direct-product")
			.log(LoggingLevel.INFO, "FindByUPC Header='${header.upc}'")
			.to("sql:select * from product where upc=:#upc?outputClass=br.com.fiap.fuse.model.Product&outputType=SelectOne&dataSource=#fiapdbDS")
			.log(LoggingLevel.INFO, "Body=${body}")
			.endRest();

		from("direct:listProducts").id("store-a-direct-list-product")
		    .log(LoggingLevel.INFO, "FindAll")
		    .to("sql:select * from product?outputClass=br.com.fiap.fuse.model.Product&dataSource=#fiapdbDS")
			.log(LoggingLevel.INFO, "Body=${body}")
			.endRest();
	}
}
