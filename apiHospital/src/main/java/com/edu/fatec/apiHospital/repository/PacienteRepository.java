package com.edu.fatec.apiHospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.fatec.apiHospital.models.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> { }
