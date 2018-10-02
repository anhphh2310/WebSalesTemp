package tma.datraining.model;

import java.sql.Timestamp;

public class ResponseMsg {

	private Timestamp time;
	private int status;
	private String msg;
	private String details;

	public ResponseMsg(Timestamp time, int status, String msg, String details) {
		super();
		this.time = time;
		this.status = status;
		this.msg = msg;
		this.details = details;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

}
