package com.example.demo.repository;

import com.example.demo.entity.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "currencies")
@CrossOrigin(origins = "http://localhost:3000")
public interface CurrenciesRepository extends JpaRepository<Currencies, String> {
}