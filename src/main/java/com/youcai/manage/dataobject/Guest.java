package com.youcai.manage.dataobject;

import com.youcai.manage.utils.EDSUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

@Entity
@Data
public class Guest implements UserDetails {

    @Id
    private String id;

    /*--- 客户密码 ---*/
    private String pwd;

    private String name;

    private String addr;

    private String phone;

    /*--- 客户负责人1手机号 ---*/
    private String mobile1;

    /*--- 客户负责人1姓名 ---*/
    private String leader1;

    /*--- 客户负责人2手机号 ---*/
    private String mobile2;

    /*--- 客户负责人2姓名 ---*/
    private String leader2;

    private String note;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return EDSUtils.decryptBasedDes(this.pwd);
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.id;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
