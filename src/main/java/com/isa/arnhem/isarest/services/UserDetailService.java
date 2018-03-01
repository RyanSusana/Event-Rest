package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserDetail;
import com.isa.arnhem.isarest.repository.UserDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Service
public class UserDetailService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<User> user = userDao.findByUsernameOrEmail(s);


        if (user.isPresent()) {
            return new UserDetail(user.get());
        }
        throw new UsernameNotFoundException("Username not found!");
    }
}
