package com.ecc.company.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String isin;
	private String ticker;
	private String companyName;
	private String companyMarket;
	private Boolean watchList;
	private Date lastUpdate;
	private String modUser;
	private Date modDate;
	
	@PrePersist
	@PreUpdate
	void company() {
		this.modDate = new Date();
	}
	
}
