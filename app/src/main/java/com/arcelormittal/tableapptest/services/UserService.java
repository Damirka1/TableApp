package com.arcelormittal.tableapptest.services;

import com.arcelormittal.tableapptest.room.entities.User;

import java.util.Objects;

public class UserService {
    private static UserService INSTANCE;

    private User user;

    public static UserService getInstance() {

        if(Objects.isNull(INSTANCE))
            INSTANCE = new UserService();

        return INSTANCE;
    }

    private void getUser() {
        // TODO: get user from room db

        user = new User();
    }

    private void saveUser() {
        // TODO: make saving
    }

    public UserService() {
        Thread t = new Thread(this::getUser);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFirstStartUp() {
        return user.isFirstStartup();
    }

    public void saveCode(String code) {
        // TODO: make code saving in room db

//        LiteDirectory ld = LiteDirectory.getInstance();

        user.setCode(code);
        user.setFirstStartup(false);

        new Thread(this::saveUser).start();
    }

    public String getCode() {
        return user.getCode();
    }

}
