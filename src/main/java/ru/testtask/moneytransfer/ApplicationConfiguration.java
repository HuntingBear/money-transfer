package ru.testtask.moneytransfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.repository.AccountRepository;

import java.math.BigDecimal;

@Configuration
public class ApplicationConfiguration {

    private final AccountRepository accountRepository;

    @Autowired
    public ApplicationConfiguration(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Preload 2 account entities into database
     */
    private void load() {
        accountRepository.save(new Account(111, BigDecimal.valueOf(1000000)));
        accountRepository.save(new Account(222, BigDecimal.valueOf(2000000)));
    }

    @Bean
    ApplicationRunner applicationRunner(ApplicationConfiguration dataLoader) {
        return (o) -> dataLoader.load();
    }

    @Configuration
    public static class CustomRepositoryRestConfigurer implements RepositoryRestConfigurer {
        /**
         * Includes ids into account entity json
         */
        @Override
        public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
            config.exposeIdsFor(Account.class);
        }
    }
}
