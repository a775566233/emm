package com.emm.service.user;

import com.emm.entity.User;
import com.emm.mapper.UserMapper;
import com.emm.util.uuid.UUIDTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findUsers(long offset, long limit) {
        return userMapper.findUser(offset, limit);
    }

    @Override
    public User findUserById(long id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    @Override
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Override
    public User findUserByPhone(String area, String phone) {
        return userMapper.findUserByPhone(area, phone);
    }

    @Override
    public List<User> findUsersByArea(String area, long offset, long limit) {
        return userMapper.findUserByArea(area, offset, limit);
    }

    @Override
    public List<User> findUserByStatus(int status, long offset, long limit) {
        return userMapper.findUserByStatus(status, offset, limit);
    }

    @Override
    public List<User> findUserByUserDuties(String duties, long offset, long limit) {
        return userMapper.findUserByUserDuties(duties, offset, limit);
    }

    @Override
    public List<User> findUserByUserUUID(String userUUID, long offset, long limit) {
        return userMapper.findUserByUserUUID(userUUID, offset, limit);
    }

    @Override
    public User findUserByLoginName(String loginName, String password) {
        User user = userMapper.findUserByLoginName(loginName, password);
        log.info(UUIDTools.getUUID(user.getUserUUID()).toString());
        return user;
    }

    @Override
    public User findUserByLoginEmail(String loginEmail, String password) {
        return userMapper.findUserByLoginEmail(loginEmail, password);
    }

    @Override
    public User findUserByLoginPhone(String area, String loginPhone, String password) {
        return userMapper.findUserByLoginPhone(area, loginPhone, password);
    }

    @Override
    public List<User> searchUserByUserName(String name, long offset, long limit) {
        return userMapper.searchUserByUserName(name, offset, limit);
    }

    @Override
    public List<User> searchUserByEmail(String email, long offset, long limit) {
        return userMapper.searchUserByUserEmail(email, offset, limit);
    }

    @Override
    public List<User> searchUserByPhone(String phone, long offset, long limit) {
        return userMapper.searchUserByUserPhone(phone, offset, limit);
    }

    @Override
    public List<User> searchUserByArea(String area, long offset, long limit) {
        return userMapper.searchUserByUserArea(area, offset, limit);
    }

    @Override
    public List<User> searchUserByRegisterStamp(long registerStamp, long offset, long limit) {
        return userMapper.searchUserByRegisterStamp(registerStamp, offset, limit);
    }

    @Override
    public List<User> searchUserByRegisterPrefixStamp(long registerPrefixStamp, long offset, long limit) {
        return userMapper.searchUserByRegisterPrefixStamp(registerPrefixStamp, offset, limit);
    }

    @Override
    public List<User> searchUserByIntervalStamp(long startStamp, long endStamp, long offset, long limit) {
        return userMapper.searchUserByIntervalStamp(startStamp, endStamp, offset, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateUser(User user) {
        if (user.getUserId() > 0) {
            return userMapper.updateUserById(user);
        } else if (user.getUserName() != null) {
           return userMapper.updateUserByName(user);
        } else if (user.getUserEmail() != null) {
            return userMapper.updateUserByEmail(user);
        } else if (user.getUserPhone() != null) {
            return userMapper.updateUserByPhone(user);
        } else if (user.getUserUUID() != null) {
            return userMapper.updateUserByUUID(user);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long deleteUser(User user) {
        if (user.getUserId() > 0) {
            return userMapper.deleteUserById(user.getUserId());
        } else if (user.getUserName() != null) {
            return userMapper.deleteUserByName(user.getUserName());
        } else if (user.getUserEmail() != null) {
            return userMapper.deleteUserByEmail(user.getUserEmail());
        } else if (user.getUserPhone() != null) {
            return userMapper.deleteUserByPhone(user.getUserArea(), user.getUserPhone());
        } else if (user.getUserUUID() != null) {
            return userMapper.deleteUserByUserUUID(user.getUserUUID());
        }
        return 0;
    }
}
