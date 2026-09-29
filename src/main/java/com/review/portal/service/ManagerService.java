package com.review.portal.service;

import com.review.portal.dto.ManagerDTO;

import java.util.List;

/**
 * Service interface for Manager domain operations.
 */
public interface ManagerService {

    List<ManagerDTO> getAllManagers();

    ManagerDTO getManagerById(Long id);

    ManagerDTO getManagerByEmail(String email);

    List<ManagerDTO> getManagersByDepartment(String department);

    ManagerDTO createManager(ManagerDTO managerDTO);

    ManagerDTO updateManager(Long id, ManagerDTO managerDTO);

    void deleteManager(Long id);
}
