package com.chinet.meethere;


import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHandlerTest extends AndroidTestCase {

    @Mock
    private DatabaseHandler db;

    @Mock
    RenamingDelegatingContext context;

    @Mock
    User fakeUser;

    @Mock
    User user;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHandler(context);
    }

    @Test
    public void isRunning() throws Exception {
        assertEquals(8, 4 + 4);
    }

    @Test
    public void addUserTest() throws Exception {
        fakeUser = new User("Matt", "Stone", "matt@example.com", "London", "17.08.1995", null);

        db.addUser(fakeUser);

        List<User> users = db.getAllUsers();

        assertNotNull("Users is null", users);
    }

    @Override
    public void tearDown() throws Exception {
        db.closeDB();
        super.tearDown();
    }
}
