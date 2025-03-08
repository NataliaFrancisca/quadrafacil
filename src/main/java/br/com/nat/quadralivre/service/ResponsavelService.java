package br.com.nat.quadralivre.service;

import br.com.nat.quadralivre.model.Responsavel;
import br.com.nat.quadralivre.repository.ResponsavelRepository;
import br.com.nat.quadralivre.util.ValidatorResponsavel;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponsavelService {
    private final ResponsavelRepository responsavelRepository;
    private final ValidatorResponsavel validatorResponsavel;

    @Autowired
    public ResponsavelService(ResponsavelRepository responsavelRepository, ValidatorResponsavel validatorResponsavel){
        this.responsavelRepository = responsavelRepository;
        this.validatorResponsavel = validatorResponsavel;
    }

    private Responsavel buscaPeloResponsavel(String cpf){
        this.validatorResponsavel.validarCPF(cpf);

        Optional<Responsavel> responsavel = this.responsavelRepository.findByCpf(cpf);

        if(responsavel.isEmpty()){
            throw new EntityNotFoundException("Não existe cadastro com esse número de CPF.");
        }

        return responsavel.get();
    }

    public Responsavel create(Responsavel responsavel){
        this.validatorResponsavel.validar(responsavel);
        return this.responsavelRepository.save(responsavel);
    }

    public Responsavel getByCPF(String cpf){
        return this.buscaPeloResponsavel(cpf);
    }

    public Responsavel updateByCPF(String cpf, Responsavel responsavelAtualizado){
        Responsavel responsavelParaAtualizar = this.buscaPeloResponsavel(cpf);

        this.validatorResponsavel.validarAtualizacao(responsavelParaAtualizar, responsavelAtualizado);

        responsavelParaAtualizar.setCpf(responsavelAtualizado.getCpf());
        responsavelParaAtualizar.setNome(responsavelAtualizado.getNome());
        responsavelParaAtualizar.setEmail(responsavelAtualizado.getEmail());
        responsavelParaAtualizar.setTelefone(responsavelAtualizado.getTelefone());

        return this.responsavelRepository.save(responsavelParaAtualizar);
    }

    public void delete(String cpf){
        buscaPeloResponsavel(cpf);
        this.responsavelRepository.deleteByCpf(cpf);
    }
}
