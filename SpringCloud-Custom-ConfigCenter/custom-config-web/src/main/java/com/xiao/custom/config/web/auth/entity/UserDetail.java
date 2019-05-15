package com.xiao.custom.config.web.auth.entity;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author : JoeTao
 * createAt: 2018/9/14
 */
public class UserDetail implements UserDetails
{
    private long id;
    private String username;
    private String password;
    private String nickname;
    private Role role;
    private Date lastPasswordResetDate;

    public UserDetail(AuthUser user)
    {
        this.id = user.getId();
        this.role = user.getRole();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
    }

    public UserDetail(AuthUser user, Role role)
    {
        this.id = user.getId();
        this.role = role;
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public UserDetail(long id, String username, Role role,
            //            Date lastPasswordResetDate,
            String password)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        //        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public UserDetail(String username, String password, Role role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDetail(long id, String username, String password)
    {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * 账户是否激活
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public Date getLastPasswordResetDate()
    {
        if (null != lastPasswordResetDate)
        {
            return new Date(lastPasswordResetDate.getTime());
        }
        return null;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate)
    {
        this.lastPasswordResetDate = new Date(lastPasswordResetDate.getTime());
    }

    public String getNickname()
    {
        return nickname;
    }
}
