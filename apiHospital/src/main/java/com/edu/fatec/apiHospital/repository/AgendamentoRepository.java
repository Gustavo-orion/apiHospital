package com.edu.fatec.apiHospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.fatec.apiHospital.models.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByData(String data);
}
