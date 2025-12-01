package com.e_commerce.e_commerce.service.categoryServiceImpl;

import com.e_commerce.e_commerce.model.UserDtls;
import com.e_commerce.e_commerce.repository.UserRepository;
import com.e_commerce.e_commerce.service.UserService;
import com.e_commerce.e_commerce.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user){
        user.setRole("ROLE_Admin");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setLockTime(null);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        UserDtls saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public UserDtls getUserByEmail(String email){
        UserDtls user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public List<UserDtls> getUsers(String role)
    {
       return  userRepository.findByRole(role);
    }

    @Override
    public Boolean updateUserAccountStatus(Integer id,Boolean status)
    {
        Optional<UserDtls> findByUser = userRepository.findById(id);
        if(findByUser.isPresent())
        {
            UserDtls userDtls = findByUser.get();
            userDtls.setIsEnable(status);
            userRepository.save(userDtls);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(UserDtls user)
    {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(UserDtls user)
    {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public Boolean accountTimeExpired(UserDtls user)
    {
        long lockTime = user.getLockTime().getTime();
        long currentTime = System.currentTimeMillis();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
        if(currentTime > unlockTime){
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return  false;
    }

    @Override
    public void resetAttempt(int userId)
    {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken){
        UserDtls user = userRepository.findByEmail(email);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    @Override
    public UserDtls getUserByResetToken(String token){
        return userRepository.findByResetToken(token);
    }

    @Override
    public UserDtls updateUser(UserDtls user)
    {
        return userRepository.save(user);
    }

}
