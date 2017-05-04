package com.larry.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.larry.model.Staff;

public interface StaffRepository extends DataTablesRepository<Staff, Long> {
}
