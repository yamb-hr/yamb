package com.tejko.yamb.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tejko.yamb.constants.SecurityConstants;

@Entity
public class Player implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = SecurityConstants.MIN_USERNAME_SIZE, max = SecurityConstants.MAX_USERNAME_SIZE)
    private String username;

    @Column
    @JsonIgnore
    private String password;
    
    @Column
    @JsonIgnore
    private Boolean tempUser;
    
    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Score> scores;
    
    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Log> logs;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Game> games;

    @ManyToMany
    @JoinTable(name = "player_role", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    protected Player() {}

    private Player(String username, String password, boolean tempUser) {
        this.username = username;
        this.password = password;
		this.tempUser = tempUser;
    }

    private Player(Long id, String username, String password, List<GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

    public static Player getInstance(String username, String password, boolean tempUser) {
        return new Player(username, password, tempUser);
    }

    public static Player build(Player player) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : player.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getLabel()));
        }
		return new Player(player.getId(), player.getUsername(), player.getPassword(), authorities);
	}

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTempUser() {   
        return tempUser;
    }

    public void setTempUser(boolean tempUser) {
        this.tempUser = tempUser;
    }

    public List<Score> getScores() {
        return scores;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Game> getGames() {
        return games;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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