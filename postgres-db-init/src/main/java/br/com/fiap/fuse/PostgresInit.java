package br.com.fiap.fuse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.fuse.entity.Product;

public class PostgresInit {
	private static Logger log = LoggerFactory.getLogger(PostgresInit.class);
	
	private static String[][] products = new String[][] {
			{"101",
			 "10",
			 "169.99"},
			{"102",
		     "20",
		     "2497.99"},
			{"103",
		     "40",
			 "1398.00"}
	};
	
    public static void main( String[] args ) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
    	EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("fiap-postgres-1");
    	EntityManager em1 = emf1.createEntityManager();
    	
    	em1.getTransaction().begin();
    	
    	for (String[] productRec : products) {
	    	Product product1 = new Product();
	    	product1.setId(UUID.randomUUID().toString());
	    	product1.setUpc(productRec[0]);
	    	product1.setQuantity(new BigInteger(productRec[1]));
	    	product1.setPrice(new BigDecimal(productRec[2]));
	    	em1.persist(product1);
    	}
    	
    	em1.flush();
    	em1.close();
    	em1.getTransaction().commit();    	
    }
}
