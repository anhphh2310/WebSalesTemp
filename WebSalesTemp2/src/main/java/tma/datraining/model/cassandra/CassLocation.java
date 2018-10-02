package tma.datraining.model.cassandra;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table("location")
public class CassLocation {

	private UUID locationId;
	private String coutry;
	private String city;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public CassLocation(UUID locationId, String coutry, String city, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.locationId = locationId;
		this.coutry = coutry;
		this.city = city;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	public CassLocation() {
		super();
	}

	@PrimaryKeyColumn(name="location_id",type=PrimaryKeyType.PARTITIONED,ordinal=1)
	@CassandraType(type=Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column(value="country")
	public String getCoutry() {
		return coutry;
	}

	public void setCoutry(String coutry) {
		this.coutry = coutry;
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

}
