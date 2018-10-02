package tma.datraining.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "sales")
public class Sales {

	private UUID salesId;
	private Product product;
	private Time time;
	private Location location;
	private BigDecimal dollars;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public Sales() {
		super();
	}

	public Sales(Product product, Time time, Location location, BigDecimal dollars, Timestamp createAt,
			Timestamp modifiedAt) {
		super();
		this.product = product;
		this.time = time;
		this.location = location;
		this.dollars = dollars;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	@Id
	@GeneratedValue
	@Column(name="sale_id",nullable=false,insertable=false)
	public UUID getSalesId() {
		return salesId;
	}

	public void setSalesId(UUID salesId) {
		this.salesId = salesId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id",updatable=false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="time_id",nullable=false)
	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", updatable=false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
	@Column(name="dollars",columnDefinition="MONEY")
	public BigDecimal getDollars() {
		return dollars;
	}

	public void setDollars(BigDecimal dollars) {
		this.dollars = dollars;
	}

	@Column(name="create_at")
	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	@Column(name="modified_at")
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
