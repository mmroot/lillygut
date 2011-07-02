import static models.Money.CHF;

import org.h2.engine.User;

import org.junit.*;

import java.util.*;

import play.db.jpa.JPABase;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	private static final String CLAUDIO_MOBILE = "+41788048273";
	
	@Before
	public void setup(){
		ConsumeEvent.deleteAll();
		MarketPrePayEvent.deleteAll();
		BuyEvent.deleteAll();
		
		Promo.deleteAll();
		Shop.deleteAll();
		Client.deleteAll();
		Market.deleteAll();
		Phone.deleteAll();
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
    	Market chiasso = chiasso().save();
    	new Shop("Pfahler SA", chiasso).save();
    	
    	Shop shopFound = Shop.find("byName", "Pfahler SA").first();
        assertEquals(chiasso, shopFound.market);
    	Market found = findChiasso();
        assertEquals(1, found.shops.size());
    }

    @Test
    public void addUser() {
    	new Phone("+41788048273").save();

        assertEquals(1,Phone.find("byMobile", "+41788048273").fetch().size());
    }

    @Test
    public void clientBuysFor100CHFandGets200Lillyguts() {
    	Phone phone = claudio();
    	Market chiasso = chiasso();
    	Shop pfahler = pfahler(chiasso);
    	
    	phone.buys(pfahler,Money.CHF(100), "Piatto antico in vetro di Murano");
    	
        assertEquals(200, phone.guts(chiasso));
    }

    @Test
    public void shopSellsFor100FrancsAndClientGets200Lillyguts() {
    	Phone phone = claudio();
    	Market chiasso = chiasso();
    	Shop pfahler = pfahler(chiasso);
    	
    	Client client = pfahler.register(phone);
		pfahler.sells(client,Money.CHF(100), "Piatto antico in vetro di Murano");
    	
        assertEquals(200, phone.guts(chiasso));
    }

    @Test
    public void buyGeneratesTransitions() {
    	Phone phone = claudio();
    	Market chiasso = chiasso();
    	Shop shop = pfahler(chiasso);
    	
    	phone.buys(shop,Money.CHF(100), "Piatto antico in vetro di Murano");
    	
        assertEquals(1, shop.transitions());
        assertEquals(1, chiasso.transitions());
        assertEquals(1, Client.findBy(phone, chiasso).transitions());
    }

    @Test
    public void marketBuys10000Lillyguts() {
    	Market chiasso = chiasso();
    	
    	chiasso.prePay(new Guts(10000));
    	
        assertEquals(10000, chiasso.guts().quantity);
        assertEquals(1, chiasso.transitions());
    }

    @Test
    public void shopAddAPromo() {
    	Market chiasso = chiasso();
    	Shop shop = pfahler(chiasso);
    	
    	shop.addPromo("Piatto antico in vetro di Murano",Money.CHF(100), Money.CHF(30), new Guts(30));
    	
    	assertEquals(1, Promo.findBy(shop).size());
        assertEquals(1, shop.promos().size());
        assertEquals(1, shop.transitions());
    	assertTrue(Promo.findBy(shop).get(0).isValid());
    }

    @Test
    public void shopWillRegisterClaudio() {
    	assertEquals(chiasso().clients().size(), 0);

    	pfahler(chiasso()).register(CLAUDIO_MOBILE);
    	
    	assertNotNull(Phone.findByMobile(CLAUDIO_MOBILE));
    	assertTrue(chiasso().hasClient(CLAUDIO_MOBILE));
    	assertEquals(chiasso().clients().size(), 1);
    }

    @Test
    public void buyingAutomaticallyRegistersClaudio() {
    	Phone cliaudiosPhone = claudio();
    	Shop shop = pfahler(chiasso());
    	
    	cliaudiosPhone.buys(shop,CHF(100), "Piatto antico in vetro di Murano");
    	
    	assertTrue(chiasso().hasClient(CLAUDIO_MOBILE));
    }

    @Test
    public void claudioUsesGutsForAPromo() {
    	Phone claudiosPhone = claudio();
		Shop shop = pfahler(chiasso());
		claudiosPhone.buys(shop,CHF(100), "Piatto antico in vetro di Murano");
    	Promo promo = shop.addPromo("Piatto nuovo di zecca",Money.CHF(100), Money.CHF(30), new Guts(30));

    	claudiosPhone.consume(promo);

        assertEquals(170, claudiosPhone.guts(chiasso()));
    	assertEquals(1, Promo.findBy(pfahler(chiasso())).size());
    	assertFalse(promo.isValid());
        assertEquals(3, pfahler(chiasso()).transitions());
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
	private Market chiasso() {
		Market chiasso = Market.findByPhone(955);
		if(chiasso == null)
			chiasso = new Market(955, "Chiasso").save();
		return chiasso;
	}

	private Market findChiasso() {
		return Market.find("byPhone", 955).first();
	}

	private Shop pfahler(Market chiasso) {
		Shop shop = Shop.findByName("Pfahler SA");
		if(shop == null)
			shop = new Shop("Pfahler SA", chiasso).save();
		return shop;
	}

	private Phone claudio() {
		return new Phone("+41788048273").save();
	}

	private Promo preparePromo(Phone phone) {
		Shop shop = pfahler(chiasso());
    	phone.buys(shop,CHF(100), "Piatto antico in vetro di Murano");
    	return shop.addPromo("Piatto nuovo di zecca",Money.CHF(100), Money.CHF(30), new Guts(30));
	}
}
