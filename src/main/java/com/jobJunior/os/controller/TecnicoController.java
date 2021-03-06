package com.jobJunior.os.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jobJunior.os.dtos.TecnicoDTO;
import com.jobJunior.os.modelo.Tecnico;
import com.jobJunior.os.services.TecnicoService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoController {

	@Autowired
	private TecnicoService tecnicoService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
		TecnicoDTO objDto = new TecnicoDTO(tecnicoService.findById(id));
		return ResponseEntity.ok().body(objDto);
	}

	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll() {
//		List<TecnicoDTO> listDTO = tecnicoService.findAll().stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());

		List<Tecnico> list = tecnicoService.findAll();
		List<TecnicoDTO> listDtos = new ArrayList<>();
		for (Tecnico tecnico : list) {
			listDtos.add(new TecnicoDTO(tecnico));
		}

		return ResponseEntity.ok().body(listDtos);
	}

	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO tecnicoDTO) {
		Tecnico novoTecnico = tecnicoService.create(tecnicoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novoTecnico.getId())
				.toUri();

		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO tecnicoDTO) {
		TecnicoDTO newTDto = new TecnicoDTO(tecnicoService.update(id, tecnicoDTO));

		return ResponseEntity.ok().body(newTDto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		tecnicoService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
