package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class MarketPrePayEvent extends Model {

	@ManyToOne
	public Market market;
	public Guts guts;

	public MarketPrePayEvent(Market market, Guts guts) {
		this.market = market;
		this.guts = guts;
	}

	public static Guts findGutsBy(Market market) {
		Guts results = new Guts();
		for (MarketPrePayEvent event : findBy(market)) 
			results = results.add(event.guts);
		return results;
	}

	public static List<MarketPrePayEvent> findBy(Market market) {
		return MarketPrePayEvent.find("byMarket", market).fetch();
	}

}
