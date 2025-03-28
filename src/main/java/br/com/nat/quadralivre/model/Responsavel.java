package br.com.nat.quadralivre.model;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "Responsavel")
@Table(name = "Responsavel")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "Responsável", description = "Operações relacionadas a responsável")
public class Responsavel {
    @Id
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;
}