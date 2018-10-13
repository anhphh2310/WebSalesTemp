package tma.datraining.model;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "location")
public class Location {

	private UUID locationId;
	private String country;
	private String city;
	private Timestamp createAt;
	private Timestamp modifiedAt;
	private Set<Sales> sales;
	
	public Location() {
		super();
	}

	public Location(String country, String city, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.country = country;
		this.city = city;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	@Id
	@Column(name="location_id")
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column(name="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	@OneToMany(fetch=FetchType.LAZY,mappedBy="location")
	@JsonIgnore
	public Set<Sales> getSales() {
		return sales;
	}

	@JsonProperty
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	}

}
