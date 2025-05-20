package com.org.service;

import com.org.dto.request.DeveloperRequest;
import com.org.dto.response.DeveloperResponse;
import com.org.exception.DuplicateResourceException;
import com.org.mapper.DeveloperMapper;
import com.org.model.Developer;
import com.org.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final DeveloperMapper developerMapper;

    @Transactional
    public DeveloperResponse create(DeveloperRequest developerRequest) {
        if (developerRepository.existsByName(developerRequest.name())) {
            throw new IllegalArgumentException("Ya existe un developer con ese nombre");
        }
        Developer saved = developerRepository.save(developerMapper.toEntity(developerRequest));
        return developerMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DeveloperResponse> findAll(){
        return developerRepository.findAll().stream().map(developerMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<DeveloperResponse> findAll(Pageable pageable) {
        return developerRepository.findAll(pageable)
                .map(developerMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public Page<DeveloperResponse> findPaginated(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return developerRepository.findAll(pageable).map(developerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public DeveloperResponse findById(Long id){
        Developer developer = developerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un developer con ese id"));
        return developerMapper.toResponse(developer);
    }

    @Transactional
    public DeveloperResponse update(Long id, DeveloperRequest developerRequest) {
        Developer developer = developerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Developer no encontrado"));
        if (!developer.getName().equals(developerRequest.name()) && developerRepository.existsByName(developerRequest.name())) {
            throw new DuplicateResourceException("Ya existe otro developer con ese nombre");
        }
        developer.setName(developerRequest.name());
        return developerMapper.toResponse(developerRepository.save(developer));
    }

    @Transactional
    public void delete(Long id){
        Developer developer = developerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Developer no encontrado"));
        developerRepository.delete(developer);
    }
}
