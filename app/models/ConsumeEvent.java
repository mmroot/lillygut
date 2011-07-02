package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class ConsumeEvent extends Model {

	@ManyToOne
	public Promo promo;
	@ManyToOne
	public Client client;

	public ConsumeEvent(Phone phone, Promo promo) {
		this.promo = promo;
		this.client = promo.market().findClientBy(phone);
	}

	public static int guts(Client client) {
		int result = 0;
		for (ConsumeEvent consume : findBy(client)) 
			result += consume.guts();
		return result ;
	}

	private int guts() {
		return promo.guts.quantity;
	}

	private static List<ConsumeEvent> findBy(Client client) {
		return ConsumeEvent.find("byClient", client).fetch();
	}

	private static List<ConsumeEvent> findBy(Promo promo) {
		return ConsumeEvent.find("byPromo", promo).fetch();
	}

	public static int guts(Promo promo) {
		int result = 0;
		for (ConsumeEvent consume : findBy(promo)) 
			result += consume.guts();
		return result ;
	}

	public static List<ConsumeEvent> findBy(Shop shop) {
		return ConsumeEvent.find("byPromo.shop", shop).fetch();
	}

}
