package com.david.gamestop.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.david.gamestop.model.GameStopSale;
import com.david.gamestop.model.GameStopSaleAggregate;
import com.david.gamestop.model.GameStopSaleImportStatus;
import com.david.gamestop.service.GameStopSaleAggregateService;
import com.david.gamestop.service.GameStopService;

@RestController
public class GameStopController {
	@Autowired
	private GameStopService gameService;

	@Autowired
	private GameStopSaleAggregateService gameStopSaleAggregateService;

	@PostMapping("/import")
	public ResponseEntity<String> importGames(@RequestParam MultipartFile file) {
		Instant start = Instant.now();

		GameStopSaleImportStatus gameStopSaleImportStatus = new GameStopSaleImportStatus();
		gameStopSaleImportStatus.setStatus("Start");
		gameStopSaleImportStatus.setImportTimestamp(new Timestamp(System.currentTimeMillis()));
		gameService.updateOrInsertGameStopSaleImportStatus(gameStopSaleImportStatus);

		if (file.isEmpty()) {
			gameStopSaleImportStatus.setStatus("Failed");
			gameStopSaleImportStatus.setErrorMessage("Empty File");
			gameService.updateOrInsertGameStopSaleImportStatus(gameStopSaleImportStatus);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a file.");
		}

		gameStopSaleImportStatus.setFileName(file.getOriginalFilename());

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

			List<GameStopSale> gameStopSaleList = new ArrayList<GameStopSale>();

			for (CSVRecord csvRecord : csvParser) {
				GameStopSale game = new GameStopSale();
				game.setId(Long.parseLong(csvRecord.get(0)));
				game.setGameNo(Integer.parseInt(csvRecord.get(1)));
				game.setGameName(csvRecord.get(2));
				game.setGameCode(csvRecord.get(3));
				game.setType(Integer.parseInt(csvRecord.get(4)));
				game.setCostPrice(new BigDecimal(csvRecord.get(5)));
				game.setTax(new BigDecimal(csvRecord.get(6)));
				game.setSalePrice(new BigDecimal(csvRecord.get(7)));
				game.setDateOfSale(Timestamp.valueOf(csvRecord.get(8)));
//				game.setDateOfSale(new Date(Timestamp.valueOf(csvRecord.get(8)).getTime()));

				gameStopSaleList.add(game);
//                gameService.createGameSale(game);
			}

//            gameService.createGameSale(gameStopSaleList);
//            gameService.createGameSaleJDBC(gameStopSaleList);
			gameService.createGameSaleJDBCThread(gameStopSaleList);

			// TODO: Update the aggregate table in the background.
//            for (GameStopSale record : gameStopSaleList) {
//            	gameStopSaleAggregateService.updateAggregatedSales(record.getDateOfSaleTimestamp(), record.getGameNo(), record.getSalePrice());
//            }

			gameStopSaleImportStatus.setStatus("Success");
			gameStopSaleImportStatus
					.setProcessingTime(Long.toString(Duration.between(start, Instant.now()).toMillis()));
			gameService.updateOrInsertGameStopSaleImportStatus(gameStopSaleImportStatus);

			return ResponseEntity.ok("Games imported successfully.");
		} catch (IOException e) {
			gameStopSaleImportStatus.setStatus("Failed");
			gameStopSaleImportStatus.setErrorMessage("IO Exception");
			gameService.updateOrInsertGameStopSaleImportStatus(gameStopSaleImportStatus);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file.");
		} catch (Exception e) {
			gameStopSaleImportStatus.setStatus("Failed");
			gameStopSaleImportStatus.setErrorMessage("Exception");
			gameService.updateOrInsertGameStopSaleImportStatus(gameStopSaleImportStatus);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing file: " + e.getMessage());
		}
	}

	@GetMapping("/getGameSales")
	public ResponseEntity<List<GameStopSale>> getGameSales(@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate, @RequestParam(required = false) BigDecimal price,
			@RequestParam(required = false) String comparison, @RequestParam(defaultValue = "0") int page) {

		// TODO: Perform validation checks for the request parameters

		List<GameStopSale> gameSales;

		if (startDate != null && endDate != null) {
			gameSales = gameService.getSalesBetweenDates(startDate, endDate, page);
		} else if ("less".equals(comparison) && price != null) {
			gameSales = gameService.getSalesLessThan(price, page);
		} else if ("greater".equals(comparison) && price != null) {
			gameSales = gameService.getSalesGreaterThan(price, page);
		} else {
			gameSales = gameService.getAllGameSales(page);
		}

		return ResponseEntity.ok(gameSales);
	}

	@GetMapping("/getTotalSales")
	public ResponseEntity<?> getTotalSales(@RequestParam Date startDate, @RequestParam Date endDate,
			@RequestParam(required = false) Integer gameNo) {

		// TODO: Perform validation checks for the request parameters

		if (gameNo != null) {
			GameStopSaleAggregate totalSalesForGame = gameStopSaleAggregateService.getDailySalesByGameNo(startDate,
					endDate, gameNo);
			return ResponseEntity.ok(totalSalesForGame);
		} else {
			List<GameStopSaleAggregate> totalSales = gameStopSaleAggregateService.getDailySalesAndGameNo(startDate,
					endDate);
			return ResponseEntity.ok(totalSales);
		}
	}
}
