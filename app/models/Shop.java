package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Shop extends Model {

	public String name;
	@ManyToOne
	public Market market;

	public Shop(String name, Market market) {
		this.name = name;
		this.market = market;
    	market.add(this);
	}

	public int transitions() {
		return buyEvents() + promoEvents() + consumeEvents();
	}

	private int buyEvents() {
		return BuyEvent.findBy(this).size();
	}

	private int promoEvents() {
		return Promo.findBy(this).size();
	}

	private int consumeEvents() {
		return ConsumeEvent.findBy(this).size();
	}

	public int toGuts(Money money) {
		return market.toGuts(money);
	}

	public Promo addPromo(String description, Money fullPrice, Money price, Guts guts) {
		return new Promo(this, description, fullPrice, price, guts).save();
	}

	public List<Promo> promos() {
		return Promo.findBy(this);
	}

	public Client register(String mobile) {
		return register(Phone.findOrCreateByMobile(mobile));
	}

	public Client register(Phone phone) {
		return new Client(phone, market).save();
	}

	public void sells(Client client, Money prize, String product) {
		new BuyEvent(this, client, prize, product).save();
	}

	public static Shop findByName(String string) {
		return Shop.find("byName", string).first();
	}
}
