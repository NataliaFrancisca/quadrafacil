package br.com.nat.quadralivre.service;

import br.com.nat.quadralivre.dto.ResponsavelDTO;
import br.com.nat.quadralivre.dto.ResponsavelSimplesDTO;
import br.com.nat.quadralivre.model.Responsavel;
import br.com.nat.quadralivre.repository.ResponsavelRepository;
import br.com.nat.quadralivre.service.validacao.ValidacaoResponsavel;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponsavelService {
    private final ResponsavelRepository responsavelRepository;
    private final ValidacaoResponsavel validacaoResponsavel;

    @Autowired
    public ResponsavelService(ResponsavelRepository responsavelRepository, ValidacaoResponsavel validacaoResponsavel){
        this.responsavelRepository = responsavelRepository;
        this.validacaoResponsavel = validacaoResponsavel;
    }

    private Responsavel buscaPeloResponsavel(String cpf){
        this.validacaoResponsavel.validarCPF(cpf);

        return this.responsavelRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Não existe cadastro com esse número de CPF."));
    }

    public ResponsavelDTO create(ResponsavelDTO responsavelDTO){
        this.validacaoResponsavel.validar(responsavelDTO);
        Responsavel responsavelParaSalvar = this.responsavelRepository.save(ResponsavelDTO.toEntity(responsavelDTO));
        return ResponsavelDTO.fromEntity(responsavelParaSalvar);
    }

    public ResponsavelDTO getByCPF(String cpf){
        Responsavel responsavel = this.buscaPeloResponsavel(cpf);
        return ResponsavelDTO.fromEntity(responsavel);
    }

    public ResponsavelDTO update(String cpf, ResponsavelSimplesDTO responsavelDTO){
        Responsavel responsavelParaAtualizar = this.buscaPeloResponsavel(cpf);

        this.validacaoResponsavel.validarAtualizacao(
                responsavelParaAtualizar,
                responsavelDTO
        );

        responsavelParaAtualizar.setNome(responsavelDTO.getNome());
        responsavelParaAtualizar.setEmail(responsavelDTO.getEmail());
        responsavelParaAtualizar.setTelefone(responsavelDTO.getTelefone());

        this.responsavelRepository.save(responsavelParaAtualizar);

        return ResponsavelDTO.fromEntity(responsavelParaAtualizar);
    }

    public void delete(String cpf){
        this.buscaPeloResponsavel(cpf);
        this.responsavelRepository.deleteByCpf(cpf);
    }
}
