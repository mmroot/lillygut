package models;

import java.util.List;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Promo extends Model {

	@ManyToOne
	public Shop shop;
	public String description;
    public Money fullPrice;
	public Money price;
	public Guts guts;

	public Promo(Shop shop, String description, Money fullPrice, Money price,
			Guts guts) {
		this.shop = shop;
		this.description = description;
		this.fullPrice = fullPrice;
		this.price = price;
		this.guts = guts;
	}

	public static List<Promo> findBy(Shop shop) {
		return Promo.find("byShop", shop).fetch();
	}

}
