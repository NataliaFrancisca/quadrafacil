package br.com.nat.quadralivre.dto;

import br.com.nat.quadralivre.enums.DiaSemana;
import br.com.nat.quadralivre.model.Funcionamento;
import br.com.nat.quadralivre.validation.DiasSemana;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionamentoDTO {
    @NotNull(message = "Dia da semana é obrigatório")
    @Enumerated(EnumType.STRING)
    @DiasSemana(message = "Digite um dia da semana válido. Ex.: SEGUNDA ou SEXTA")
    private DiaSemana diaSemana;

    @NotNull(message = "Horário de abertura é obrigatório.")
    private LocalTime abertura;

    @NotNull(message = "Horário de fechamento é obrigatório.")
    private LocalTime fechamento;

    private boolean disponibilidade = true;

    @NotNull(message = "Id da quadra é obrigatório")
    private Long quadraId;

    public static FuncionamentoDTO fromEntity(Funcionamento funcionamento){
        FuncionamentoDTO funcionamentoDTO = new FuncionamentoDTO();

        funcionamentoDTO.setDiaSemana(funcionamento.getDiaSemana());
        funcionamentoDTO.setAbertura(funcionamento.getAbertura());
        funcionamentoDTO.setFechamento(funcionamento.getFechamento());
        funcionamentoDTO.setDisponibilidade(funcionamento.getDisponibilidade());

        return funcionamentoDTO;
    }
}
