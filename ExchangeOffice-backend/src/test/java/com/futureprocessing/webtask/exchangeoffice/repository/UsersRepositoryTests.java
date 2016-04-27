package com.futureprocessing.webtask.exchangeoffice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.futureprocessing.webtask.exchangeoffice.Application;
import com.futureprocessing.webtask.exchangeoffice.model.Authorities;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(UsersRepositoryTests.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UsersRepositoryTests.DATASET })
@DirtiesContext
public class UsersRepositoryTests {

    protected static final String DATASET = "classpath:datasets/users.xml";

    private static final String FIRST_USER = "user1";
    private static final String SECOND_USER = "user2";
    private static final String THIRD_USER = "user3";
    private static final String FOURTH_USER = "user4";
    private static final String FIFTH_USER = "user5";
    private static final String USERNAME_FIELD = "username";

    @Autowired
    private UserService userService;

    @Test
    public void findAllUsers() {
        assertThat(userService.findAllUsers())
                .hasSize(4)
                .extracting(USERNAME_FIELD)
                .containsOnly(FIRST_USER, SECOND_USER, THIRD_USER, FOURTH_USER);
    }

    @Test
    public void findByUsername() {
        assertThat(userService.findByUsername(FIRST_USER))
                .extracting(USERNAME_FIELD)
                .containsOnly(FIRST_USER);
    }

    @Test
    public void isUserExist() {
        assertThat(userService.isUserExist(new Users(FIRST_USER, "", true)))
                .isTrue();

        assertThat(userService.isUserExist(new Users(FIFTH_USER, "", true)))
                .isFalse();
    }

    @Test
    public void saveUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteAllUsers() {
    }

    @Test
    public void deleteUserByUsername() {
    }

}
