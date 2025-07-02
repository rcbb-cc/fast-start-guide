package cc.rcbb.liquibase.demo.service;

import jakarta.annotation.PostConstruct;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <p>
 * TestService
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/7/2
 */
@Component
public class TestService {

    @Autowired
    private SpringLiquibase liquibase;
    @PostConstruct
    public void checkLiquibase() {
        System.out.println("Liquibase Bean: " + liquibase);
    }

}
