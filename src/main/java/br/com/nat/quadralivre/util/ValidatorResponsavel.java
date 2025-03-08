package br.com.nat.quadralivre.util;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.nat.quadralivre.exceptions.CPFInvalidoException;
import br.com.nat.quadralivre.model.Responsavel;
import br.com.nat.quadralivre.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorResponsavel {
    private final ResponsavelRepository responsavelRepository;

    @Autowired
    public ValidatorResponsavel(ResponsavelRepository responsavelRepository){
        this.responsavelRepository = responsavelRepository;
    }

    private void validarUnicidade(String campo, String valor, boolean existe){
        if(existe){
            throw new DataIntegrityViolationException("Já existe um cadastro com esse " + campo + ": " + valor);
        }
    }

    private void verificarCpfExistente(String cpf){
        validarUnicidade("CPF", cpf, responsavelRepository.existsByCpf(cpf));
    }

    private void verificarEmailExistente(String email){
        validarUnicidade("e-mail", email, responsavelRepository.existsByEmail(email));
    }

    private void verificarTelefoneExistente(String telefone){
        validarUnicidade("telefone", telefone, responsavelRepository.existsByTelefone(telefone));
    }

    public void validar(Responsavel responsavel){
        this.verificarCpfExistente(responsavel.getCpf());
        this.verificarEmailExistente(responsavel.getEmail());
        this.verificarTelefoneExistente(responsavel.getTelefone());
    }

    public void validarAtualizacao(Responsavel responsavel, Responsavel responsavelAtualizado){
        if(!responsavel.getCpf().equals(responsavelAtualizado.getCpf())){
            this.verificarCpfExistente(responsavelAtualizado.getCpf());
        }

        if(!responsavel.getEmail().equals(responsavelAtualizado.getEmail())){
            this.verificarEmailExistente(responsavelAtualizado.getEmail());
        }

        if(!responsavel.getTelefone().equals(responsavelAtualizado.getTelefone())){
            this.verificarTelefoneExistente(responsavelAtualizado.getTelefone());
        }
    }

    public void validarCPF(String cpf){
        CPFValidator cpfValidator = new CPFValidator();
        List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf);

        if(!erros.isEmpty()){
            throw new CPFInvalidoException("Digite um CPF válido, somente os números.");
        }
    }
}
