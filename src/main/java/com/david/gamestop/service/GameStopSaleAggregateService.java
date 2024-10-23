package com.david.gamestop.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.gamestop.model.GameStopSaleAggregate;
import com.david.gamestop.repository.GameStopSaleAggregateRepository;

@Service
public class GameStopSaleAggregateService {
	@Autowired
	private GameStopSaleAggregateRepository gameStopSaleAggregate;
	
	public List<GameStopSaleAggregate> getDailySalesAndGameNo(Date startDate, Date endDate) {
	    return gameStopSaleAggregate.findByDateOfSaleBetween(startDate, endDate);
	}
	
	public GameStopSaleAggregate getDailySalesByGameNo(Date startDate, Date endDate, Integer gameNo) {
	    return gameStopSaleAggregate.findByDateOfSaleBetweenAndTotalGameNo(startDate, endDate, gameNo);
	}
	
	// Insert or update aggregated sales data
    public void updateAggregatedSales(Date date, Integer gameNo, BigDecimal salePrice) {
    	List<GameStopSaleAggregate> aggregationList = gameStopSaleAggregate
                .findByDateOfSaleBetween(date, date);
    	
    	GameStopSaleAggregate aggregation = aggregationList.getFirst();
        if (aggregation == null) {
            // Create a new record if none exists
            aggregation = new GameStopSaleAggregate();
            aggregation.setDateOfSale(date);
            aggregation.setTotalGameNo(gameNo);
            aggregation.setTotalSales(salePrice);
        } else {
            // Update existing record
            aggregation.setTotalSales(aggregation.getTotalSales().add(salePrice));
            aggregation.setTotalGameNo(aggregation.getTotalGameNo()+1);
        }

        gameStopSaleAggregate.save(aggregation);
    }
	
}
