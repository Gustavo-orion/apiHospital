package com.edu.fatec.apiHospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.fatec.apiHospital.models.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
