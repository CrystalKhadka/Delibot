package com.system.online_ordering_system.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.online_ordering_system.entity.Qr;

@Repository
public interface QrRepo extends JpaRepository<Qr,Integer>{

}
