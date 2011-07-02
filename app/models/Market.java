package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Market extends Model {

	public String name;
	public int phone;
	
	@OneToMany(mappedBy="market", cascade=CascadeType.ALL)
	public Set<Shop> shops;

	public Market(int phone, String name) {
		shops = new HashSet<Shop>();
		this.phone = phone;
		this.name = name;
	}

	public void add(Shop shop) {
		shops.add(shop);
		shop.market = this;
	}

	public int toGuts(Money money) {
		return money.multiply(2).intValue();
	}

	public int transitions() {
		return clientBuyTransitions() + prePayTransitions();
	}

	private int prePayTransitions() {
		return MarketPrePayEvent.findBy(this).size();
	}

	private int clientBuyTransitions() {
		return BuyEvent.transitionsBy(this);
	}

	public void prePay(Guts guts) {
		new MarketPrePayEvent(this, guts).save();
	}

	public Guts guts() {
		return MarketPrePayEvent.findGutsBy(this);
	}

	public List<Client> clients() {
		return Client.findBy(this);
	}

	public static Market findByPhone(int shortCode) {
		return Market.find("byPhone", shortCode).first();
	}

	public boolean hasClient(String mobile) {
		Phone thePhone = Phone.findByMobile(mobile);
		if(thePhone == null)
			return false;
		return Client.findBy(thePhone, this) != null;
	}

	public Client findClientBy(Phone phone) {
		return Client.findBy(phone, this);
	}

}
