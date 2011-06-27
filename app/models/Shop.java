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
		return clientBuyEvents() + promoEvents();
	}

	private int promoEvents() {
		return Promo.findBy(this).size();
	}

	private int clientBuyEvents() {
		return BuyEvent.transitionsBy(this);
	}

	public int toGuts(Money money) {
		return market.toGuts(money);
	}

	public void addPromo(String description, Money fullPrice, Money price, Guts guts) {
		new Promo(this, description, fullPrice, price, guts).save();
	}

	public List<Promo> promos() {
		return Promo.findBy(this);
	}
}
