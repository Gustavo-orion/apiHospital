package com.edu.fatec.apiHospital.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.fatec.apiHospital.models.Medico;
import com.edu.fatec.apiHospital.repository.MedicoRepository;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Medico> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medico> salvar(@RequestBody Medico medico) {
        Medico salvo = repository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizar(@PathVariable Long id, @RequestBody Medico medico) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Chama a stored procedure para atualizar
        jdbcTemplate.update(
            "CALL atualizar_medico(?, ?, ?, ?, ?, ?)",
            id,
            medico.getNome(),
            medico.getCpf(),
            medico.getCrm(),
            medico.getEspecialidade(),
            medico.getTelefone()
        );

        // Retorna o médico atualizado
        return repository.findById(id)
                .map(ResponseEntity::ok)
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
