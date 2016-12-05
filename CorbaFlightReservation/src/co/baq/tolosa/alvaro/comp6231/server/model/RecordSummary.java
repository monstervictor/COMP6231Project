package co.baq.tolosa.alvaro.comp6231.server.model;

import java.io.Serializable;

public class RecordSummary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1383133266870638909L;
	private String city;
	private Integer totalRecords;
	
	public RecordSummary(){
		this("default", 0);
	}
	
	public RecordSummary(String city){
		this(city, 0);
	}
	
	public RecordSummary(String city, Integer numRecords){
		this.setCity(city);
		this.setTotalRecords(numRecords);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}
