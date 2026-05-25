package com.edu.fatec.apiHospital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.fatec.apiHospital.models.Paciente;
import com.edu.fatec.apiHospital.repository.PacienteRepository;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @GetMapping
    public List<Paciente> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Paciente> salvar(@RequestBody Paciente paciente) {
        // Lógica simples para gerar o ID visual #XXXX se não vier do App
        if (paciente.getPacienteId() == null) {
            paciente.setPacienteId("#" + (int)(Math.random() * 9000 + 1000));
        }
        Paciente salvo = repository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(paciente.getNome());
                    existente.setIdade(paciente.getIdade());
                    existente.setPacienteId(paciente.getPacienteId());
                    existente.setUltimaVisita(paciente.getUltimaVisita());
                    existente.setStatus(paciente.getStatus());
                    Paciente atualizado = repository.save(existente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}