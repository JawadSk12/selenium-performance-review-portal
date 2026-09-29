package com.review.portal.service.impl;

import com.review.portal.dto.ManagerDTO;
import com.review.portal.repository.ManagerRepository;
import com.review.portal.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Service implementation skeleton for Manager operations.
 * Business logic to be finalized in Phase 2.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public List<ManagerDTO> getAllManagers() {
        log.info("Fetching all managers");
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    public ManagerDTO getManagerById(Long id) {
        log.info("Fetching manager by id: {}", id);
        // Business logic to be implemented in Phase 2
        return null;
    }

    @Override
    public ManagerDTO getManagerByEmail(String email) {
        log.info("Fetching manager by email: {}", email);
        // Business logic to be implemented in Phase 2
        return null;
    }

    @Override
    public List<ManagerDTO> getManagersByDepartment(String department) {
        log.info("Fetching managers by department: {}", department);
        // Business logic to be implemented in Phase 2
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        log.info("Creating manager with email: {}", managerDTO.getEmail());
        // Business logic to be implemented in Phase 2
        return managerDTO;
    }

    @Override
    @Transactional
    public ManagerDTO updateManager(Long id, ManagerDTO managerDTO) {
        log.info("Updating manager id: {}", id);
        // Business logic to be implemented in Phase 2
        return managerDTO;
    }

    @Override
    @Transactional
    public void deleteManager(Long id) {
        log.info("Deleting manager id: {}", id);
        // Business logic to be implemented in Phase 2
    }
}
