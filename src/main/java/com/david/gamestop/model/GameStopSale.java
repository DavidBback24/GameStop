package com.david.gamestop.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table (name="game_sales")
public class GameStopSale {
	@Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_sales_seq")
//    @SequenceGenerator(name = "game_sales_seq", sequenceName = "game_sales_seq", initialValue = 1)
	private Long id;
	private Integer gameNo;
	private String gameName;
	private String gameCode;
	private Integer type;
	private BigDecimal costPrice;
	private BigDecimal tax = BigDecimal.valueOf(9.00);
	private BigDecimal salePrice;
	private Timestamp dateOfSale;
//	private Date dateOfSale;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the gameNo
	 */
	public Integer getGameNo() {
		return gameNo;
	}

	/**
	 * @param gameNo the gameNo to set
	 */
	public void setGameNo(Integer gameNo) {
		this.gameNo = gameNo;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the gameCode
	 */
	public String getGameCode() {
		return gameCode;
	}

	/**
	 * @param gameCode the gameCode to set
	 */
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the costPrice
	 */
	public BigDecimal getCostPrice() {
		return costPrice;
	}

	/**
	 * @param costPrice the costPrice to set
	 */
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * @return the salePrice
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Timestamp getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(Timestamp dateOfSale) {
		this.dateOfSale = dateOfSale;
	}
}