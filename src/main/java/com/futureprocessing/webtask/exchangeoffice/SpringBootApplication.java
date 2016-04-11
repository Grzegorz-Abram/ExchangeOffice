package com.futureprocessing.webtask.exchangeoffice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

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
public class SpringBootApplication {

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @Autowired
    ExchangeRateService exchangeRateService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UsersRepository usersRepository, AuthoritiesRepository authoritiesRepository, WalletsRepository walletsRepository) {
        return (args) -> {
            usersRepository.save(new Users("user", "$2a$10$IzWi1i14KuUu239KDpGStuuTi67bNIfzgrttFoK7o0aynENIOGIqa", true));
            usersRepository.save(new Users("bank", "", false));

            authoritiesRepository.save(new Authorities("user", "USER"));
            authoritiesRepository.save(new Authorities("bank", ""));

            walletsRepository.save(new Wallets("bank", "USD", 1000));
            walletsRepository.save(new Wallets("bank", "EUR", 1000));
            walletsRepository.save(new Wallets("bank", "CHF", 1000));
            walletsRepository.save(new Wallets("bank", "RUB", 1000));
            walletsRepository.save(new Wallets("bank", "CZK", 1000));
            walletsRepository.save(new Wallets("bank", "GBP", 1000));
        };
    }

}
