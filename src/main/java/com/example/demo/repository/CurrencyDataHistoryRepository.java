package com.example.demo.repository;

import com.example.demo.entity.CurrencyDataHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@RepositoryRestResource(path = "currency_data_history")
public interface CurrencyDataHistoryRepository extends JpaRepository<CurrencyDataHistory, UUID> {
}
