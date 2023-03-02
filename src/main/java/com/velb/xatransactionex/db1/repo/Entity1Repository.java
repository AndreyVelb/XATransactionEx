package com.velb.xatransactionex.db1.repo;

import com.velb.xatransactionex.db1.model.Entity1;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface Entity1Repository extends JpaRepository<Entity1, Long>{




}
