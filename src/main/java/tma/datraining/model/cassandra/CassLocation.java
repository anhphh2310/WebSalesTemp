package tma.datraining.model.cassandra;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.DataType.Name;

@Table("location")
public class CassLocation {

	private UUID locationId;
	private String country;
	private String city;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public CassLocation(UUID locationId, String country, String city, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.locationId = locationId;
		this.country = country;
		this.city = city;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	@PrimaryKeyColumn(name="location_id",type = PrimaryKeyType.PARTITIONED)
	@CassandraType(type=DataType.Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column(value="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(value="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(value = "create_at")
	@CassandraType(type = Name.TIMESTAMP)
	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	@Column(value = "modified_at")
	@CassandraType(type = Name.TIMESTAMP)
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
