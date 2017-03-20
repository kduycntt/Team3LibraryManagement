package com.team3.LMS.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.team3.LMS.dao.UserInfoDao;
import com.team3.LMS.dto.Role;
import com.team3.LMS.dto.UserInfo;
@Service
@Transactional
public class UserInfoService implements UserDetailsService{
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userInfoDao.findByEmail(username);
		if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPword(), grantedAuthorities);
	}
	public List<UserInfo> getUserInfoList() {
		return (List<UserInfo>) userInfoDao.findAll();
	}

	public Page<UserInfo> findAll(Pageable pageable) {
		return userInfoDao.findAll(pageable);
	}

	public void addUserInfo(UserInfo userInfo) {
		userInfoDao.save(userInfo);
	}

	public void removeUserInfo(int id) {
		userInfoDao.delete(id);
	}

	public UserInfo getUserInfo(int id) {
		return userInfoDao.findOne(id);
	}
}
