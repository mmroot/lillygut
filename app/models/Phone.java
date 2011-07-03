package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.jpa.Model;

@Entity
public class Phone extends Model {

	public String mobile;

	public Phone(String mobile) {
		this.mobile = mobile;
	}

	public void buys(Shop shop, Money amount, String text) {
		new BuyEvent(shop, this, amount, text).save();
	}

	public int guts(Market market) {
		return Client.findBy(this, market).guts();
	}

	public static Phone findByMobile(String mobile) {
		return Phone.find("byMobile", mobile).first();
	}

	public static Phone findOrCreateByMobile(String mobile) {
		Phone phone = findByMobile(mobile);
		if (phone == null)
			phone = new Phone(mobile).save();
		return phone;
	}

	public void consume(Promo promo) {
		if (!promo.isValid())
			throw new IllegalArgumentException("The promo already consumed.");
		new ConsumeEvent(this, promo).save();

	}

}
