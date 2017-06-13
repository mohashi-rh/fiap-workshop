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

public class MongoInit {
	private static Logger log = LoggerFactory.getLogger(MongoInit.class);
	
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
    	TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
    	EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("fiap-mongo-1");
    	EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("fiap-mongo-2");
    	
    	EntityManager em1 = emf1.createEntityManager();
    	EntityManager em2 = emf2.createEntityManager();
    	
    	tm.begin();
    	
    	for (String[] productRec : products) {
	    	Product product1 = new Product();
	    	product1.setId(UUID.randomUUID().toString());
	    	product1.setUpc(productRec[0]);
	    	product1.setQuantity(new BigInteger(productRec[1]));
	    	product1.setPrice(new BigDecimal(productRec[2]));
	    	em1.persist(product1);
	    	
	    	Product product2 = new Product();
	    	product2.setId(UUID.randomUUID().toString());
	    	product2.setUpc(productRec[0]);
	    	product2.setQuantity(new BigInteger(productRec[1]));
	    	product2.setPrice(new BigDecimal(productRec[2]).add(new BigDecimal("1000")));
	    	em2.persist(product2);
    	}
    	
    	em1.flush();
    	em1.close();
    	em2.flush();
    	em2.close();
    	tm.commit();
    }
}
