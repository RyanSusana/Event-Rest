package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.UserDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Service
public class UserService {

    private final UserDao userDao;

    private final EventDao eventDao;
}
