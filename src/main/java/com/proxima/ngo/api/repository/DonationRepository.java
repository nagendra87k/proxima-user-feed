package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.Donations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donations,Long> {
}
