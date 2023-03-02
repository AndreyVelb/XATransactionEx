package com.velb.xatransactionex.service;

import com.velb.xatransactionex.db1.model.Entity1;
import com.velb.xatransactionex.db1.repo.Entity1Repository;
import com.velb.xatransactionex.db2.model.Entity2;
import com.velb.xatransactionex.db2.repo.Entity2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@ComponentScan("com.velb.xatransactionex")
public class MyService {

    private final Entity1Repository entity1Repository;
    private final Entity2Repository entity2Repository;


    @Transactional
    public void saveEntity1() {
        Entity1 entity1 = Entity1.builder()
                .name("Alex")
                .balanсe(100)
                .build();
        entity1Repository.save(entity1);

    }

    @Transactional
    public void saveEntity2() {
        Entity2 entity2 = Entity2.builder()
                .id(1L)
                .name("Igor")
                .balance(200)
                .build();
        entity2Repository.save(entity2);
    }
}
