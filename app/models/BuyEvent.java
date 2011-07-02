package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class BuyEvent extends Model {

	@ManyToOne
	public Shop shop;
	@ManyToOne
	public Client client;
	public Money money;
	public String description;
	public int guts;

	public BuyEvent(Shop shop, Phone phone, Money prize, String product) {
		this(shop, shop.register(phone), prize, product);
	}

	public BuyEvent(Shop shop, Client client, Money prize, String product) {
		this.shop = shop;
		this.client = client;
		this.money = prize;
		this.description = product;
		this.guts = shop.toGuts(money);
	}

	public static List<BuyEvent> findBy(Shop shop) {
		return find("byShop", shop).fetch();
	}

	public static int transitionsBy(Market market) {
		return findBy(market).size();
	}

	private static List<Object> findBy(Market market) {
		return BuyEvent.find("byShop.market", market).fetch();
	}

	public static List<BuyEvent> findBy(Client client) {
		return find("byClient", client).fetch();
	}

	public static int guts(Client client) {
		int result = 0;
		for (BuyEvent buy : findBy(client)) 
			result += buy.guts;
		return result ;
	}

}
