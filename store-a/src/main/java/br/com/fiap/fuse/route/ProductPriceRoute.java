package br.com.fiap.fuse.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

import br.com.fiap.fuse.model.MongoProduct;
import br.com.fiap.fuse.processor.MongoToProductProcessor;


public class ProductPriceRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		restConfiguration()
			.dataFormatProperty("prettyPrint", "true")
			.enableCORS(true)
			.contextPath("api/v1")
				.component("netty4-http").host("0.0.0.0").port(8080)
			.bindingMode(RestBindingMode.json)
			.apiContextPath("/api-doc")
				.apiProperty("api.title", "Product API").apiProperty("api.version", "1")
				.apiProperty("cors", "true");;
		
		rest("/product").id("store-a-rest-product")
			.get().description("List all products")
				.produces("application/json")
				.to("direct:listProducts")
			.get("/{upc}").description("Retrieve product")
				.produces("application/json")
				.to("direct:product");
		
		JacksonDataFormat jsonDF = new JacksonDataFormat();
		jsonDF.setUnmarshalType(MongoProduct.class);
		
		from("direct:product").id("store-a-direct-product")
			.log(LoggingLevel.INFO, "FindByUPC Header='${header.upc}'")
			.transform().simple("{ \"upc\": \"${header.upc}\" }")
			.to("mongodb:mongoBean?database=fiap-db-1&collection=product&operation=findOneByQuery")
			.convertBodyTo(String.class)
			.unmarshal(jsonDF)
			.process(new MongoToProductProcessor(true))
			.log(LoggingLevel.INFO, "Body=${body}")
			.endRest();

		JacksonDataFormat jsonListDF = new JacksonDataFormat();
		jsonListDF.setUseList(true);
		jsonListDF.setUnmarshalType(MongoProduct.class);

		from("direct:listProducts").id("store-a-direct-list-product")
		    .log(LoggingLevel.INFO, "FindAll")
			.to("mongodb:mongoBean?database=fiap-db-1&collection=product&operation=findAll")
			.convertBodyTo(String.class)
			.unmarshal(jsonListDF)
			.process(new MongoToProductProcessor(false))
			.log(LoggingLevel.INFO, "Body=${body}")
			.endRest();
	}
}
