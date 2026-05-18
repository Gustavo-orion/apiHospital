package com.edu.fatec.apiHospital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.fatec.apiHospital.models.Medico;
import com.edu.fatec.apiHospital.repository.MedicoRepository;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*") // Permite que o Android acesse sem bloqueio de CORS
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @GetMapping
    public List<Medico> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Medico> salvar(@RequestBody Medico medico) {
        Medico salvo = repository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
