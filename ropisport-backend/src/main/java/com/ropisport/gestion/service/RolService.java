package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.RolRequest;
import com.ropisport.gestion.model.dto.response.RolResponse;

import java.util.List;

public interface RolService {
    List<RolResponse> getAllRoles();
    RolResponse getRolById(Integer id);
    RolResponse createRol(RolRequest rolRequest);
    RolResponse updateRol(Integer id, RolRequest rolRequest);
    void deleteRol(Integer id);
}