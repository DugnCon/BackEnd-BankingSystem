package com.damdung.banking.security.utils;

import com.damdung.banking.model.dto.MyUserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityUtils {
    /**
     * Lấy thông tin của người dùng
     * */
    public static MyUserDetail getPrincipal() {
        return (MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Lấy quyền của người dùng
     * */
    public static List<String> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority authority : grantedAuthorities) {
            authorities.add(authority.getAuthority());
        }
        return authorities;
    }
}
