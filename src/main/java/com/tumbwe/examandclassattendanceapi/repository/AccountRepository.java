package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
