package com.mybank.accountmanagement.context;

import com.mybank.accountmanagement.models.User;
import com.mybank.accountmanagement.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class SpringSecurityUserContextService implements UserContextService {

    private UserRepository userRepository;

    public SpringSecurityUserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Optional<User> u = userRepository.findById(getCurrentUserId());
        if( !u.isPresent() ){
            throw new RuntimeException("Current user does not exist");
        }else{
            return u.get();
        }
    }

    @Override
    public Long getCurrentUserId() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( !(p instanceof UserDetails) ){
            throw new RuntimeException("Current user does not exist");
        }else{
            UserDetails userDetails = (UserDetails)p;
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if( !user.isPresent() ){
                throw new UsernameNotFoundException(userDetails.getUsername());
            }
            return user.get().getId();
        }
    }

    @Override
    public String getCurrentUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( username == null ){
            throw new RuntimeException("Current user does not exist");
        }else{
            return username;
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}