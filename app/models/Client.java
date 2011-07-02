package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Client extends Model{

	@ManyToOne
	public Phone phone;
	@ManyToOne
	public Market market;

	public Client(Phone phone, Market market) {
		this.phone = phone;
		this.market = market;
	}

	public static List<Client> findBy(Market market) {
		return find("byMarket", market).fetch();
	}

	public static Client findBy(Phone phone, Market market) {
		return find("byPhoneAndMarket", phone, market).first();
	}
	
	public int transitions() {
		return BuyEvent.findBy(this).size();
	}

	public int guts() {
		return BuyEvent.guts(this)-ConsumeEvent.guts(this);
	}

}
