package com.david.gamestop.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.*;
import com.david.gamestop.model.GameStopSale;
import com.david.gamestop.model.GameStopSaleImportStatus;
import com.david.gamestop.repository.GameStopImportRepository;
import com.david.gamestop.repository.GameStopRepository;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class GameStopService {
	@Autowired
	private GameStopRepository gameStopRepository;

	@Autowired
	private GameStopImportRepository gameStopImportRepository;

	@Autowired
	HikariDataSource hikariDataSource;

	public List<GameStopSale> createGameSale(List<GameStopSale> gameStopSaleList) {
		List<List<GameStopSale>> subLists = splitArrayList(gameStopSaleList, 1000);
		for (List<GameStopSale> subList : subLists) {
			gameStopRepository.saveAllAndFlush(subList);
		}
		return null;
	}

	public void createGameSaleJDBC(List<GameStopSale> gameStopSaleList) {
		String sql = "INSERT INTO game_sales (id, game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = hikariDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			// Disable auto-commit mode
			connection.setAutoCommit(false);
			int batchSize = 30;

			int counter = 0;
			for (GameStopSale record : gameStopSaleList) {
				preparedStatement.clearParameters();
				preparedStatement.setLong(1, record.getId());
				preparedStatement.setInt(2, record.getGameNo());
				preparedStatement.setString(3, record.getGameName());
				preparedStatement.setString(4, record.getGameCode());
				preparedStatement.setInt(5, record.getType());
				preparedStatement.setBigDecimal(6, record.getCostPrice());
				preparedStatement.setBigDecimal(7, record.getTax());
				preparedStatement.setBigDecimal(8, record.getSalePrice());
				preparedStatement.setTimestamp(9, record.getDateOfSale());
//				preparedStatement.setDate(10, record.getDateOfSale());

				// Add to batch
				preparedStatement.addBatch();

				if ((counter + 1) % batchSize == 0 || (counter + 1) == gameStopSaleList.size()) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}
				counter++;
			}

			// Commit the transaction
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createGameSaleJDBCThread(List<GameStopSale> gameStopSaleList) {
		ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
		List<List<GameStopSale>> gameStopSaleSubList = splitArrayList(gameStopSaleList, 1000);
		List<Callable<Void>> callables = gameStopSaleSubList.stream().map(sublist -> (Callable<Void>) () -> {
			createGameSaleJDBC(sublist);
			return null;
		}).collect(Collectors.toList());
		try {
			executorService.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public GameStopSaleImportStatus updateOrInsertGameStopSaleImportStatus(
			GameStopSaleImportStatus gameStopSaleImportStatus) {
		return gameStopImportRepository.save(gameStopSaleImportStatus);
	}

	public List<GameStopSale> getAllGameSales(int page) {
		Pageable pageable = PageRequest.of(page, 100);
		return gameStopRepository.findAll(pageable).getContent();
	}

	public List<GameStopSale> getSalesBetweenDates(Date startDate, Date endDate, int page) {
		Pageable pageable = PageRequest.of(page, 100);
		return gameStopRepository.findSalesBetweenDates(startDate, endDate, pageable);
	}

	public List<GameStopSale> getSalesLessThan(BigDecimal price, int page) {
		Pageable pageable = PageRequest.of(page, 100);
		return gameStopRepository.findBySalePriceLessThan(price, pageable);
	}

	public List<GameStopSale> getSalesGreaterThan(BigDecimal price, int page) {
		Pageable pageable = PageRequest.of(page, 100);
		return gameStopRepository.findBySalePriceGreaterThan(price, pageable);
	}

	public static <T> List<List<T>> splitArrayList(List<T> originalList, int chunkSize) {
		List<List<T>> splitLists = new ArrayList<>();
		for (int i = 0; i < originalList.size(); i += chunkSize) {
			splitLists.add(new ArrayList<>(originalList.subList(i, Math.min(i + chunkSize, originalList.size()))));
		}
		return splitLists;
	}
}
