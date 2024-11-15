package com.example.demo.repository;

import com.example.demo.entity.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "currencies")
public interface CurrenciesRepository extends JpaRepository<Currencies, UUID> {
}