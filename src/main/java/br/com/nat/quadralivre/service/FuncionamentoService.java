package br.com.nat.quadralivre.service;

import br.com.nat.quadralivre.dto.FuncionamentoDTO;
import br.com.nat.quadralivre.dto.FuncionamentoQuadraDTO;
import br.com.nat.quadralivre.enums.DiaSemana;
import br.com.nat.quadralivre.model.Funcionamento;
import br.com.nat.quadralivre.repository.FuncionamentoRepository;
import br.com.nat.quadralivre.repository.QuadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionamentoService {
    final private FuncionamentoRepository funcionamentoRepository;
    final private QuadraRepository quadraRepository;

    @Autowired
    public FuncionamentoService(FuncionamentoRepository funcionamentoRepository, QuadraRepository quadraRepository){
        this.funcionamentoRepository = funcionamentoRepository;
        this.quadraRepository = quadraRepository;
    }

    private void validarQuadra(Long quadraId){
        this.quadraRepository.findById(quadraId)
                .orElseThrow(() -> new EntityNotFoundException("Não existe quadra com esse número de ID"));
    }

    private Funcionamento buscaPeloFuncionamentoEFazValidacao(Long id){
        Optional<Funcionamento> funcionamentoParaAtualizarOptional = this.funcionamentoRepository.findById(id);

        if(funcionamentoParaAtualizarOptional.isEmpty()){
            throw new EntityNotFoundException("Não existe cadastro de funcionamento com esse ID.");
        }

        return funcionamentoParaAtualizarOptional.get();
    }

    private List<DiaSemana> buscarPelosDiasCadastrados(Long quadraId){
        return this.funcionamentoRepository.findAllByQuadraId(quadraId)
                .stream()
                .map(Funcionamento::getDia_semana)
                .toList();
    }

    private Funcionamento converterParaEntidade(Long quadraId, FuncionamentoDTO funcionamentoDTO){
        return new Funcionamento(quadraId, funcionamentoDTO.getDiaSemana(), funcionamentoDTO.getAbertura(), funcionamentoDTO.getFechamento());
    }

    public List<Funcionamento> create(Long quadraId, List<FuncionamentoDTO> funcionamentoDTOS){
        this.validarQuadra(quadraId);

        List<DiaSemana> diasCadastrados = this.buscarPelosDiasCadastrados(quadraId);

        List<Funcionamento> funcionamentos = funcionamentoDTOS
                .stream()
                .filter(dto -> !diasCadastrados.contains(dto.getDiaSemana()))
                .map(dto -> this.converterParaEntidade(quadraId, dto))
                .toList();

        if(funcionamentos.isEmpty()){
            throw new IllegalArgumentException("Nenhum valor foi adicionado. Dias da semana que já existem são ignorados.");
        }

        return this.funcionamentoRepository.saveAll(funcionamentos);
    }

    public FuncionamentoQuadraDTO getAll(Long quadraId){
        this.validarQuadra(quadraId);

        List<FuncionamentoDTO> funcionamentos = this.funcionamentoRepository.findAllByQuadraId(quadraId)
                .stream()
                .map(FuncionamentoDTO::fromEntity)
                .toList();

        return new FuncionamentoQuadraDTO(quadraId, funcionamentos);
    }

    public FuncionamentoDTO update(FuncionamentoDTO funcionamentoDTO){
        Funcionamento funcionamento = this.buscaPeloFuncionamentoEFazValidacao(funcionamentoDTO.getId());

        funcionamento.setAbertura(funcionamentoDTO.getAbertura());
        funcionamento.setFechamento(funcionamentoDTO.getFechamento());

        return FuncionamentoDTO.fromEntity(this.funcionamentoRepository.save(funcionamento));
    }

    public FuncionamentoDTO updateDisponibilidade(Long id, boolean disponibilidade){
        Funcionamento funcionamento = this.buscaPeloFuncionamentoEFazValidacao(id);

        if(Boolean.TRUE.equals(funcionamento.getDisponibilidade()) == disponibilidade){
            throw new IllegalArgumentException("Você está tentando salvar o mesmo dado já cadastrado.");
        }

        funcionamento.setDisponibilidade(disponibilidade);
        return FuncionamentoDTO.fromEntity(this.funcionamentoRepository.save(funcionamento));
    }

    public void delete(Long id){
        this.buscaPeloFuncionamentoEFazValidacao(id);
        this.funcionamentoRepository.deleteById(id);
    }
}
