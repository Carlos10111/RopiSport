package com.ropisport.gestion.service;

import java.util.List;

import com.ropisport.gestion.model.dto.request.RolRequest;
import com.ropisport.gestion.model.dto.response.RolResponse;

public interface RolService {
    List<RolResponse> getAllRoles();
    RolResponse getRolById(Integer id);
    RolResponse createRol(RolRequest rolRequest);
    RolResponse updateRol(Integer id, RolRequest rolRequest);
    void deleteRol(Integer id);
}