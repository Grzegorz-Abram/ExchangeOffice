package com.futureprocessing.webtask.exchangeoffice.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.futureprocessing.webtask.exchangeoffice.PersistenceContext;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.h2.tools.RunScript;
import static org.h2.engine.Constants.UTF8;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceContext.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DirtiesContext
@DatabaseSetup(UserServiceTests.DATASET_USERS)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UserServiceTests.DATASET_USERS })
public class UserServiceTests {

    protected static final String DATABASE_CREATE_SCRIPT = "classpath:db/datasets/users.xml";
    protected static final String DATASET_USERS = "classpath:db/datasets/users.xml";

    private static final String FIRST_USER = "user1";
    private static final String SECOND_USER = "user2";
    private static final String THIRD_USER = "user3";
    private static final String FOURTH_USER = "user4";
    private static final String FIFTH_USER = "user5";
    private static final String USERNAME_FIELD = "username";

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void createSchema() throws Exception {
        RunScript.execute("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "sa", "classpath:db/sql/create-db.sql", UTF8, false);
    }

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
