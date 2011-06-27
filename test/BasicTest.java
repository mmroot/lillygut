import org.h2.engine.User;

import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Before
	public void setup(){
		Promo.deleteAll();
		MarketPrePayEvent.deleteAll();
		BuyEvent.deleteAll();
		Shop.deleteAll();
		Market.deleteAll();
		Client.deleteAll();
	}

    @Test
    public void canStoreAndRetreiveMarket() {
    	Market market = new Market(955, "Chiasso");
		market.save();
    	
    	Market found = findChiasso();
        assertEquals("Chiasso", found.name);
    }

    @Test
    public void addShopToTheMarket() {
    	Market chiasso = chiassoMarket().save();
    	new Shop("Pfahler SA", chiasso).save();
    	
    	Shop shopFound = Shop.find("byName", "Pfahler SA").first();
        assertEquals(chiasso, shopFound.market);
    	Market found = findChiasso();
        assertEquals(1, found.shops.size());
    }

    @Test
    public void addUser() {
    	new Client("+41788048273").save();

        assertEquals(1,Client.find("byMobile", "+41788048273").fetch().size());
    }

    @Test
    public void clientBuysFor100CHFandGets200Lillyguts() {
    	Client client = claudio();
    	Market chiasso = chiassoMarket();
    	Shop pfahler = pfahler(chiasso);
    	
    	client.buys(pfahler,Money.CHF(100), "Piatto antico in vetro di Murano");
    	
        assertEquals(200, client.guts(chiasso));
    }

    @Test
    public void buyGeneratesTransitions() {
    	Client client = claudio();
    	Market chiasso = chiassoMarket();
    	Shop shop = pfahler(chiasso);
    	
    	client.buys(shop,Money.CHF(100), "Piatto antico in vetro di Murano");
    	
        assertEquals(1, shop.transitions());
        assertEquals(1, chiasso.transitions());
        assertEquals(1, client.transitions());
    }

    @Test
    public void marketBuys10000Lillyguts() {
    	Market chiasso = chiassoMarket();
    	
    	chiasso.prePay(new Guts(10000));
    	
        assertEquals(10000, chiasso.guts().quantity);
        assertEquals(1, chiasso.transitions());
    }

    @Test
    public void shopAddAPromo() {
    	Market chiasso = chiassoMarket();
    	Shop shop = pfahler(chiasso);
    	
    	shop.addPromo("Piatto antico in vetro di Murano",Money.CHF(100), Money.CHF(30), new Guts(30));
    	
        assertEquals(1, shop.promos().size());
        assertEquals(1, shop.transitions());
    }

    @Test
    @Ignore
    public void marketCommission() {
    }
    
    /*
     * 
     * 
     * 
     * 
     */
	private Market chiassoMarket() {
		return new Market(955, "Chiasso").save();
	}

	private Market findChiasso() {
		return Market.find("byPhone", 955).first();
	}

	private Shop pfahler(Market chiasso) {
		return new Shop("Pfahler SA", chiasso).save();
	}

	private Client claudio() {
		return new Client("+41788048273").save();
	}
}
