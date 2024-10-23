package com.david.gamestop.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.david.gamestop.model.GameStopSale;

public interface GameStopRepository extends JpaRepository<GameStopSale, Long> {
	@Query("SELECT g FROM GameStopSale g WHERE g.dateOfSale BETWEEN :startDate AND :endDate")
	List<GameStopSale> findSalesBetweenDates(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, Pageable pageable);

	List<GameStopSale> findBySalePriceLessThan(BigDecimal price, Pageable pageable);

	List<GameStopSale> findBySalePriceGreaterThan(BigDecimal price, Pageable pageable);
}