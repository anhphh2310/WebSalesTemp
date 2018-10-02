package tma.datraining.model.cassandra;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table("time")
public class CassTime {

	private UUID timeId;
	private int month;
	private int quarter;
	private int year;
	private Timestamp createAt;
	private Timestamp modifiedAt;

	public CassTime(UUID timeId, int month, int quarter, int year, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.timeId = timeId;
		this.month = month;
		this.quarter = quarter;
		this.year = year;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}

	public CassTime() {
		super();
	}

	@PrimaryKeyColumn(name="time_id",type=PrimaryKeyType.PARTITIONED,ordinal=1)
	@CassandraType(type=Name.UUID)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}

	@Column(value="month")
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Column(value="quarter")
	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	@Column(value="year")
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Column(value="create_at")
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	@Column(value="modified_at")
	@CassandraType(type=Name.TIMESTAMP)
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "CassTime [timeId=" + timeId + ", month=" + month + ", quarter=" + quarter + ", year=" + year
				+ ", createAt=" + createAt + ", modifiedAt=" + modifiedAt + "]";
	}

	
}
