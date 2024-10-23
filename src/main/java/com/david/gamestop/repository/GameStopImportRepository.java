package com.david.gamestop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.gamestop.model.GameStopSaleImportStatus;

public interface GameStopImportRepository extends JpaRepository<GameStopSaleImportStatus, Long> {
	
}
