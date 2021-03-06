package com.jobJunior.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobJunior.os.dtos.TecnicoDTO;
import com.jobJunior.os.modelo.Pessoa;
import com.jobJunior.os.modelo.Tecnico;
import com.jobJunior.os.repository.PessoaRepository;
import com.jobJunior.os.repository.TecnicoRepository;
import com.jobJunior.os.services.exception.DataIntegratyViolationException;
import com.jobJunior.os.services.exception.ObjectNotFoundException;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado, de Id: " + id + " Tipo: " + Tecnico.class.getName()));
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO tecnicoDTO) {
		if (findByCPF(tecnicoDTO) != null) {
			throw new DataIntegratyViolationException("CPF ja cadastrado na base de dados");
		}
		return tecnicoRepository
				.save(new Tecnico(null, tecnicoDTO.getNome(), tecnicoDTO.getCpf(), tecnicoDTO.getTelefone()));
	}

	public Tecnico update(Integer id, @Valid TecnicoDTO tecnicoDTO) {
		Tecnico tecAtual = findById(id);

		if (findByCPF(tecnicoDTO) != null && findByCPF(tecnicoDTO).getId() != id) {
			throw new DataIntegratyViolationException("O Id não confere com o CPF!");
		}
		tecAtual.setNome(tecnicoDTO.getNome());
		tecAtual.setCpf(tecnicoDTO.getCpf());
		tecAtual.setTelefone(tecnicoDTO.getTelefone());

		return tecnicoRepository.save(tecAtual);
	}

	public void delete(Integer id) {
		Tecnico tecnico = findById(id);

		if (tecnico.getListOs().size() != 0) {
			throw new DataIntegratyViolationException(
					"O tecnico não pode ser excluido, pois esta associado a Ordem de Sserviço!");
		}
		tecnicoRepository.deleteById(id);
	}

	private Pessoa findByCPF(TecnicoDTO tecnicoDTO) {
		Pessoa pessoa = pessoaRepository.findByCPF(tecnicoDTO.getCpf());
		if (pessoa != null) {
			return pessoa;
		}
		return null;
	}

}
