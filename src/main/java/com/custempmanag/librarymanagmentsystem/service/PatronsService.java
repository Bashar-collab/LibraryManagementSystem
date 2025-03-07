package com.custempmanag.librarymanagmentsystem.service;

import com.custempmanag.librarymanagmentsystem.dto.PatronsDTO;
import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.models.Patrons;
import com.custempmanag.librarymanagmentsystem.repository.PatronsRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatronsService {
    @Autowired
    private ModelMapper modelMapper;

    private final PatronsRepository patronsRepository;

    public PatronsService(PatronsRepository patronsRepository) {
        this.patronsRepository = patronsRepository;
    }

    @Cacheable(value = "patronsCache", key = "'allPatrons'")
    public MessageResponse getAllPatrons() {
        List<Patrons> patrons = patronsRepository.findAll();
        if(patrons.isEmpty())
            return new MessageResponse(HttpStatus.NOT_FOUND.toString(), "No partons found!", null);

        List<PatronsDTO> patronsDTOs = patrons.stream()
                .map(PatronsDTO::new)
                .collect(Collectors.toList());

        return new MessageResponse(HttpStatus.OK.toString(), "Patrons retrieved successfully!", patronsDTOs);
    }

    @Cacheable(value = "patronsCache", key = "#id")
    public MessageResponse getPatronById(Long id) {
        Patrons patrons = patronsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Patron not found!"));

        PatronsDTO patronsDTO = new PatronsDTO(patrons);
        return new MessageResponse(HttpStatus.OK.toString(), "Patron retrieved successfully!", patronsDTO);
    }

    @CacheEvict(value = "patronsCache", key = "'allPatrons'")
    @Transactional
    public MessageResponse addPatron(PatronsDTO patronsDTO) {
        if(patronsRepository.existsByName(patronsDTO.getName()))
            throw new CustomException("A Patron with the same name already exists");

        Patrons patrons = modelMapper.map(patronsDTO, Patrons.class);
        patronsRepository.save(patrons);

        return new MessageResponse(HttpStatus.CREATED.toString(), "Patron added successfully!", patrons);
    }

    @CacheEvict(value = "patronsCache", key = "#id")
    @Transactional
    public MessageResponse updatePatron(Long id, PatronsDTO patronsDTO) {
        Optional<Patrons> existingPatronOptional = patronsRepository.findById(id);
        if(existingPatronOptional.isEmpty())
            throw new CustomException("Patron not found!");

        Patrons existingPatrons = existingPatronOptional.get();
        existingPatrons.setName(patronsDTO.getName());
        existingPatrons.setContactInfo(patronsDTO.getContactInfo());
        patronsRepository.save(existingPatrons);

        return new MessageResponse(HttpStatus.OK.toString(), "Patron updated successfully!", existingPatrons);
    }

    @CacheEvict(value = "patronsCache", key = "#id")
    @Transactional
    public MessageResponse deletePatron(Long id) {
        Optional<Patrons> existingPatronOptional = patronsRepository.findById(id);
        if(existingPatronOptional.isEmpty())
            throw new CustomException("Book not found!");

        Patrons existingPatrons = existingPatronOptional.get();
        patronsRepository.delete(existingPatrons);

        return new MessageResponse(HttpStatus.OK.toString(), "Patron deleted successfully!", null);
    }
}
