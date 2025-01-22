package com.example.jiranbulletinboard.Domain.Vo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }
}
