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
@Table(name = "time")
public class Time {

	private UUID timeId;
	private int month;
	private int quarter;
	private int year;
	private Timestamp createAt;
	private Timestamp modifiedAt;
	private Set<Sales> sales;
	
	public Time() {
		super();
	}

	public Time(int month, int quarter, int year, Timestamp createAt, Timestamp modifiedAt) {
		super();
		this.month = month;
		this.quarter = quarter;
		this.year = year;
		this.createAt = createAt;
		this.modifiedAt = modifiedAt;
	}



	@Id
	@Column(name="time_id", nullable=false, insertable=false)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}

	@Column(name="month")
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
	@Column(name="quarter")
	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	@Column(name="year")
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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

	@OneToMany(fetch=FetchType.LAZY,mappedBy="time")
	@JsonIgnore
	public Set<Sales> getSales() {
		return sales;
	}

	@JsonProperty
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	};

	
}
