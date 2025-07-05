package com.vamikastore.vamika.repositories;

import com.vamikastore.vamika.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
