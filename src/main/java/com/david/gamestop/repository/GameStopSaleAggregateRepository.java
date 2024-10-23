package com.david.gamestop.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.gamestop.model.GameStopSaleAggregate;

public interface GameStopSaleAggregateRepository extends JpaRepository<GameStopSaleAggregate, Long> {
	// GameStopSaleAggregate findByDateOfSaleB(Date date, Date endDate);
	GameStopSaleAggregate findByDateOfSaleBetweenAndTotalGameNo(Date date, Date endDate, Integer gameNo);

	List<GameStopSaleAggregate> findByDateOfSaleBetween(Date startDate, Date endDate);
}
