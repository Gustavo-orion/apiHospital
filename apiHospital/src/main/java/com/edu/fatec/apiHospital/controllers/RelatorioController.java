package com.edu.fatec.apiHospital.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorio")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Relatório do dia via stored procedure.
     * Retorna resumo (total, finalizados, em espera) + lista de agendamentos.
     * 
     * Uso: GET /api/relatorio/dia?data=2026-06-07
     */
    @GetMapping("/dia")
    public Map<String, Object> relatorioDia(@RequestParam String data) {
        Map<String, Object> resultado = new HashMap<>();

        // Chama a procedure — primeiro result set: resumo
        Map<String, Object> resumo = jdbcTemplate.queryForMap(
            "SELECT " +
            "  COUNT(*) AS total_consultas, " +
            "  SUM(CASE WHEN a.status = 'concluido' THEN 1 ELSE 0 END) AS finalizados, " +
            "  SUM(CASE WHEN a.status = 'em_espera' THEN 1 ELSE 0 END) AS em_espera " +
            "FROM agendamentos a WHERE a.data = ?",
            data
        );

        resultado.put("totalConsultas", ((Number) resumo.get("total_consultas")).intValue());
        resultado.put("finalizados", resumo.get("finalizados") != null ? ((Number) resumo.get("finalizados")).intValue() : 0);
        resultado.put("emEspera", resumo.get("em_espera") != null ? ((Number) resumo.get("em_espera")).intValue() : 0);

        // Segundo: lista de agendamentos do dia
        List<Map<String, Object>> agendamentos = jdbcTemplate.queryForList(
            "SELECT " +
            "  a.id, a.horario, a.tipo, a.status, " +
            "  p.nome AS paciente_nome, " +
            "  m.nome AS medico_nome, " +
            "  m.especialidade AS medico_especialidade " +
            "FROM agendamentos a " +
            "LEFT JOIN pacientes p ON a.paciente_id = p.id " +
            "LEFT JOIN medicos m ON a.medico_id = m.id " +
            "WHERE a.data = ? " +
            "ORDER BY a.horario ASC",
            data
        );

        resultado.put("agendamentos", agendamentos);
        resultado.put("data", data);

        return resultado;
    }
}
