package com.david.gamestop.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_sales_import_status")
public class GameStopSaleImportStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_sales_import_status_seq")
	@SequenceGenerator(name = "game_sales_import_status_seq", sequenceName = "game_sales_import_status_seq", allocationSize = 1)
	private Long id;
	private Timestamp importTimestamp;
	private String fileName;
	private String status;
	private String errorMessage;
	private String processingTime;

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
	 * @return the importTimestamp
	 */
	public Timestamp getImportTimestamp() {
		return importTimestamp;
	}

	/**
	 * @param importTimestamp the importTimestamp to set
	 */
	public void setImportTimestamp(Timestamp importTimestamp) {
		this.importTimestamp = importTimestamp;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}
}
