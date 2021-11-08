package com.loja.LojaPw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loja.LojaPw.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByEmail(String email);

    Funcionario findByEmailAndTokenPassword(String email, String token);

}
