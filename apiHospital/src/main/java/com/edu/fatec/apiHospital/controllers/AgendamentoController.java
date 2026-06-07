package com.edu.fatec.apiHospital.controllers;

import java.util.List;
import java.util.Map;

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

import com.edu.fatec.apiHospital.models.Agendamento;
import com.edu.fatec.apiHospital.models.Medico;
import com.edu.fatec.apiHospital.models.Paciente;
import com.edu.fatec.apiHospital.repository.AgendamentoRepository;
import com.edu.fatec.apiHospital.repository.MedicoRepository;
import com.edu.fatec.apiHospital.repository.PacienteRepository;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um agendamento.
     * Espera JSON com: pacienteId, medicoId, data, horario, tipo, status (opcional), observacoes (opcional)
     */
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Map<String, Object> body) {
        try {
            Long pacienteId = Long.valueOf(body.get("pacienteId").toString());
            Long medicoId = Long.valueOf(body.get("medicoId").toString());

            Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
            Medico medico = medicoRepository.findById(medicoId).orElse(null);

            if (paciente == null) {
                return ResponseEntity.badRequest().body("Paciente não encontrado com ID: " + pacienteId);
            }
            if (medico == null) {
                return ResponseEntity.badRequest().body("Médico não encontrado com ID: " + medicoId);
            }

            Agendamento agendamento = new Agendamento();
            agendamento.setPaciente(paciente);
            agendamento.setMedico(medico);
            agendamento.setData(body.getOrDefault("data", "").toString());
            agendamento.setHorario(body.getOrDefault("horario", "").toString());
            agendamento.setTipo(body.getOrDefault("tipo", "Consulta").toString());
            agendamento.setStatus(body.getOrDefault("status", "em_espera").toString());
            agendamento.setObservacoes(body.get("observacoes") != null ? body.get("observacoes").toString() : null);

            Agendamento salvo = repository.save(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar agendamento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return repository.findById(id)
                .map(existente -> {
                    if (body.containsKey("pacienteId")) {
                        Long pacienteId = Long.valueOf(body.get("pacienteId").toString());
                        pacienteRepository.findById(pacienteId).ifPresent(existente::setPaciente);
                    }
                    if (body.containsKey("medicoId")) {
                        Long medicoId = Long.valueOf(body.get("medicoId").toString());
                        medicoRepository.findById(medicoId).ifPresent(existente::setMedico);
                    }
                    if (body.containsKey("data")) {
                        existente.setData(body.get("data").toString());
                    }
                    if (body.containsKey("horario")) {
                        existente.setHorario(body.get("horario").toString());
                    }
                    if (body.containsKey("tipo")) {
                        existente.setTipo(body.get("tipo").toString());
                    }
                    if (body.containsKey("status")) {
                        existente.setStatus(body.get("status").toString());
                    }
                    if (body.containsKey("observacoes")) {
                        existente.setObservacoes(body.get("observacoes") != null ? body.get("observacoes").toString() : null);
                    }

                    Agendamento atualizado = repository.save(existente);
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
