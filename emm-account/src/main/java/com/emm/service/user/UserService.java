package com.emm.service.user;

import com.emm.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    List<User> findUsers(long offset, long limit);
    User findUserById(long id);
    User findUserByName(String name);
    User findUserByEmail(String email);
    User findUserByPhone(String area, String phone);
    List<User> findUsersByArea(String area, long offset, long limit);
    List<User> findUserByStatus(int status, long offset, long limit);
    List<User> findUserByUserDuties(String duties, long offset, long limit);
    List<User> findUserByUserUUID(String userUUID, long offset, long limit);

    User findUserByLoginName(String loginName, String password);
    User findUserByLoginEmail(String loginEmail, String password);
    User findUserByLoginPhone(String area, String loginPhone, String password);

    List<User> searchUserByUserName(String name, long offset, long limit);
    List<User> searchUserByEmail(String email, long offset, long limit);
    List<User> searchUserByPhone(String phone, long offset, long limit);
    List<User> searchUserByArea(String area, long offset, long limit);
    List<User> searchUserByRegisterStamp(long registerStamp, long offset, long limit);
    List<User> searchUserByRegisterPrefixStamp(long registerPrefixStamp, long offset, long limit);
    List<User> searchUserByIntervalStamp(long startStamp, long endStamp, long offset, long limit);

    long addUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    long updateUser(User user);
    long deleteUser(User user);
}
