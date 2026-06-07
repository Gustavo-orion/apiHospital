package com.edu.fatec.apiHospital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        // Stored procedure para atualizar médico
        jdbcTemplate.execute("DROP PROCEDURE IF EXISTS atualizar_medico");

        String sqlMedico = """
            CREATE PROCEDURE atualizar_medico(
                IN p_id BIGINT,
                IN p_nome VARCHAR(255),
                IN p_cpf VARCHAR(20),
                IN p_crm VARCHAR(50),
                IN p_especialidade VARCHAR(100),
                IN p_telefone VARCHAR(20)
            )
            BEGIN
                UPDATE medicos
                SET nome = p_nome,
                    cpf = p_cpf,
                    crm = p_crm,
                    especialidade = p_especialidade,
                    telefone = p_telefone
                WHERE id = p_id;
            END
            """;

        jdbcTemplate.execute(sqlMedico);
    }
}
