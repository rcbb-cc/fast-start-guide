package cc.rcbb.springsecurity.demo.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SmsUserDetailsServiceImpl
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/5
 */
@Service
public class SmsUserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
