package com.xiao.custom.config.web.auth.service.impl;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.web.auth.entity.UserDetail;
import com.xiao.custom.config.web.feign.auth.AuthFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: 登陆身份认证
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 10:51
 * @since JDK 1.8
 */
@Service("configUserDetailsService")
public class ConfigUserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private AuthFeign authApi;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @exception UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AuthUser authUser = authApi.findByUsername(username);
        if (authUser == null)
        {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", username));
        }
        return new UserDetail(authUser);
    }
}
