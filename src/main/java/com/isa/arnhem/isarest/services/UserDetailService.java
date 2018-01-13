package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserDetail;
import com.isa.arnhem.isarest.repository.UserDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Service
public class UserDetailService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userDao.findByUsernameOrEmail(s);

        if(user == null){
            return null;
        }
        return new UserDetail(user);
    }
}
