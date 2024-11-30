package com.tripwhiz.tripwhizuserback.luggage.repository;

import com.tripwhiz.tripwhizuserback.luggage.entity.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageRepository extends JpaRepository<Luggage, Long> {
}
