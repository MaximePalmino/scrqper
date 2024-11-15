package com.example.demo.repository;

import com.example.demo.entity.CurrencyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "currency_data")
public interface CurrencyDataRepository extends JpaRepository<CurrencyData, UUID> {
}
