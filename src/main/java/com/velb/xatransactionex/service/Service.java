package com.velb.xatransactionex.service;

import com.velb.xatransactionex.db1.model.Entity1;
import com.velb.xatransactionex.db1.repo.Entity1Repository;
import com.velb.xatransactionex.db2.model.Entity2;
import com.velb.xatransactionex.db2.repo.Entity2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@ComponentScan("com.velb.xatransactionex")
public class Service {

    private final Entity1Repository entity1Repository;
    private final Entity2Repository entity2Repository;


    @Transactional(transactionManager = "firstTransactionManager")
    public void saveEntity1() {
        Entity1 entity1 = Entity1.builder()
                .name("Ivan")
                .balanсe(50)
                .build();
        entity1Repository.save(entity1);
        log.info("Save entity1 with id - {}", entity1.getId());
    }

    @Transactional(transactionManager = "secondTransactionManager")
    public void saveEntity2() {
        Entity2 entity2 = Entity2.builder()
                .name("Andrej")
                .balance(40)
                .build();
        entity2Repository.save(entity2);
        log.info("Save entity2 with id - {}", entity2.getId());
    }

    @Transactional(transactionManager = "transactionManager")
    public void saveDoubleEntity() {
        Entity2 entity2 = Entity2.builder()
                .name("Alexandr")
                .balance(150)
                .build();
        entity2Repository.save(entity2);
        log.info("Save entity2 with id - {}", entity2.getId());
        Entity1 entity1 = Entity1.builder()
                .name("Max")
                .balanсe(130)
                .build();
        entity1Repository.save(entity1);
        log.info("Save entity1 with id - {}", entity1.getId());
    }

    @Transactional(transactionManager = "transactionManager")
    public void transferFunds() {
        Entity1 entity1 = entity1Repository.findById(2L).orElseThrow(RuntimeException::new);
        Entity2 entity2 = entity2Repository.findById(52L).orElseThrow(RuntimeException::new);
        int entity1BalanceBeforeTransfer = entity1.getBalanсe();
        int entity2BalanceBeforeTransfer = entity2.getBalance();

        entity1.setBalanсe(entity1.getBalanсe() - 10);
        entity1Repository.save(entity1);
        log.info("Change entity1 id = {} balance from: {} to: {}",
                entity1.getId(),
                entity1BalanceBeforeTransfer,
                entity1.getBalanсe());

        entity2.setBalance(entity2.getBalance() + 10);
        entity2Repository.save(entity2);
        log.info("Change entity2 id = {} balance from: {} to: {}",
                entity2.getId(),
                entity2BalanceBeforeTransfer,
                entity2.getBalance());
    }

}
