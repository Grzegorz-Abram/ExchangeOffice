package com.futureprocessing.webtask.exchangeoffice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.futureprocessing.webtask.exchangeoffice.model.Authorities;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.repository.AuthoritiesRepository;
import com.futureprocessing.webtask.exchangeoffice.repository.UsersRepository;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;
import com.futureprocessing.webtask.exchangeoffice.service.ExchangeRateService;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
@ComponentScan("com.futureprocessing.webtask.exchangeoffice")
@EnableCaching
@PropertySource(value = "classpath:application.default.properties")
public class SpringBootApplication {

    @Autowired
    private Environment environment;

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @Autowired
    ExchangeRateService exchangeRateService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UsersRepository usersRepository, AuthoritiesRepository authoritiesRepository, WalletsRepository walletsRepository) {
        return (args) -> {
            String username = environment.getRequiredProperty("default.user.username");
            String password = environment.getRequiredProperty("default.user.password");
            String role = environment.getRequiredProperty("default.user.role");
            String bank = environment.getRequiredProperty("default.bank.username");

            usersRepository.save(new Users(username, passwordEncoder.encode(password), true));
            usersRepository.save(new Users(bank, null, false));

            authoritiesRepository.save(new Authorities(username, role));
            authoritiesRepository.save(new Authorities(bank, null));

            walletsRepository.save(new Wallets(bank, "USD", environment.getRequiredProperty("default.bank.amount.USD", Integer.class)));
            walletsRepository.save(new Wallets(bank, "EUR", environment.getRequiredProperty("default.bank.amount.EUR", Integer.class)));
            walletsRepository.save(new Wallets(bank, "CHF", environment.getRequiredProperty("default.bank.amount.CHF", Integer.class)));
            walletsRepository.save(new Wallets(bank, "RUB", environment.getRequiredProperty("default.bank.amount.RUB", Integer.class)));
            walletsRepository.save(new Wallets(bank, "CZK", environment.getRequiredProperty("default.bank.amount.CZK", Integer.class)));
            walletsRepository.save(new Wallets(bank, "GBP", environment.getRequiredProperty("default.bank.amount.GBP", Integer.class)));
        };
    }

}
