package com.org.mapper;

import com.org.model.Developer;
import com.org.dto.request.DeveloperRequest;
import com.org.dto.response.DeveloperResponse;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapper {

    // Lo que se necesita pasar a la api
    public Developer toEntity(DeveloperRequest request){
        Developer developer = new Developer();
        developer.setName(request.name());
        return developer;
    }

    // Respuesta que nos dara la api
    public DeveloperResponse toResponse(Developer developer){
        return new DeveloperResponse(developer.getId(), developer.getName());
    }
}
