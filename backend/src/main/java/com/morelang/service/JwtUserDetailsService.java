package com.morelang.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.morelang.dto.Member;
import com.morelang.repository.MemberRepository;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 유저별 권한 확인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         Optional<Member> m = memberRepository.findByUserid(username);
         Member member = m.get();
        // ArrayList로 저장해야 role들이 각각 저장되지 않고, 연이어 저장이 가능하다.
        List<GrantedAuthority> roles = new ArrayList<>();

        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if ((member.getRole()).equals("ROLE_ADMIN")) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(member.getUserid(), member.getPassword(), roles);
    }

}
