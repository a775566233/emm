package com.emm.mapper;

import com.emm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<User> findUser(@Param("offset")long offset, @Param("limit")long limit);
    User findUserById(@Param("userId")long userId);
    User findUserByName(@Param("userName")String userName);
    User findUserByEmail(@Param("userEmail")String userEmail);
    List<User> findUserByArea(@Param("usersArea")String usersArea, @Param("offset")long offset, @Param("limit")long limit);
    User findUserByPhone(@Param("usersArea")String userArea, @Param("userPhone")String userPhone);
    List<User> findUserByStatus(@Param("status")int usersArea, @Param("offset")long offset, @Param("limit")long limit);
    List<User> findUserByUserDuties(@Param("userDuties")String userDuties, @Param("offset")long offset, @Param("limit")long limit);
    List<User> findUserByUserUUID(@Param("userUUID")String userUUID, @Param("offset")long offset, @Param("limit")long limit);

    User findUserByLoginName(@Param("userName")String userName, @Param("userPassword")String userPassword);
    User findUserByLoginEmail(@Param("userEmail")String userEmail, @Param("userPassword")String userPassword);
    User findUserByLoginPhone(@Param("userArea")String userArea, @Param("userPhone")String userPhone, @Param("userPassword")String userPassword);

    List<User> searchUserByUserName(@Param("userName")String userName, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByUserPhone(@Param("userPhone")String userPhone, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByUserEmail(@Param("userEmail")String userEmail, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByUserArea(@Param("userArea")String userArea, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByRegisterStamp(@Param("registerStamp")long registerStamp, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByRegisterPrefixStamp(@Param("prefixStamp")long prefixStamp, @Param("offset")long offset, @Param("limit")long limit);
    List<User> searchUserByIntervalStamp(@Param("startStamp")long startStamp, @Param("endStamp")long endStamp, @Param("offset")long offset, @Param("limit")long limit);

    long addUser(@Param("user")User user);
    long updateUserById(@Param("user")User user);
    long updateUserByName(@Param("user")User user);
    long updateUserByEmail(@Param("user")User user);
    long updateUserByPhone(@Param("user")User user);
    long updateUserByUUID(@Param("user")User user);
    long deleteUserById(@Param("userId")long userId);
    long deleteUserByName(@Param("userName")String userName);
    long deleteUserByEmail(@Param("userEmail")String userEmail);
    long deleteUserByPhone(@Param("status")String usersArea, @Param("userPhone")String userPhone);
    long deleteUserByUserUUID(@Param("userUUID")String userUUID);
}
