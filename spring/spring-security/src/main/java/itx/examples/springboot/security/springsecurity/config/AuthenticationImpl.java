package itx.examples.springboot.security.springsecurity.config;

import itx.examples.springboot.security.springsecurity.services.dto.RoleId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AuthenticationImpl implements Authentication {

    private final String name;
    private final Collection<GrantedAuthorityImpl> grantedAuthorities;

    public AuthenticationImpl(String name, Set<RoleId> roles) {
        this.name = name;
        List<GrantedAuthorityImpl> authorities = new ArrayList<>();
        roles.forEach(r->{
            authorities.add(new GrantedAuthorityImpl(r.getId()));
        });
        this.grantedAuthorities = Collections.unmodifiableCollection(authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return name;
    }
}
