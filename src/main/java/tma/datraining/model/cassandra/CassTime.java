package tma.datraining.model.cassandra;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

@Table("time")
public class CassTime {

	private UUID timeId;
	private int month;
	private int quarter;
	private int year;
	private DateTime createAt;
	private DateTime modifiedAt;

	public CassTime(UUID timeId, int month, int quarter, int year) {
		super();
		this.timeId = timeId;
		this.month = month;
		this.quarter = quarter;
		this.year = year;
		this.createAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
		this.modifiedAt = new DateTime(DateTimeZone.forID("Asia/Saigon"));
	}

	@PrimaryKey(value="time_id")
	@CassandraType(type=DataType.Name.UUID)
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
	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	@Column(value="modified_at")
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
