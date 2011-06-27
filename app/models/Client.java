package models;

import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Client extends Model {

	public String mobile;

	public Client(String mobile) {
		this.mobile = mobile;
	}

	public void buys(Shop shop, Money amount, String text) {
		new BuyEvent(shop, this, amount, text).save();
	}

	public int guts(Market market) {
		int result = 0;
		List<BuyEvent> buys = buys();
		for (BuyEvent buy : buys) 
			result += buy.guts;
		return result ;
	}

	private List<BuyEvent> buys() {
		return BuyEvent.findBy(this);
	}


	public int transitions() {
		return BuyEvent.transitionsBy(this);
	}

}
