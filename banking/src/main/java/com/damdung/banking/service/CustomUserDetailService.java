package com.damdung.banking.service;

import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.repository.IAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private IAuthRepository iAuthRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        AuthEntity auth = iAuthRepository.findByEmail(username);
        if(auth == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(auth.getRole());
        MyUserDetail myUserDetail = new MyUserDetail(username, auth.getPassword(), true, true, true, true, List.of(grantedAuthority));

        myUserDetail.setUserID(auth.getUserID());
        myUserDetail.setFirstName(auth.getFirstName());
        myUserDetail.setRole(auth.getRole());
        myUserDetail.setEmail(auth.getEmail());

        return myUserDetail;
    }
}
