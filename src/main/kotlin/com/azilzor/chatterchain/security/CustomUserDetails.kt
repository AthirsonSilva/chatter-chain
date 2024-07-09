package com.azilzor.chatterchain.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomUserDetails(
    private var username: String,
    private var name: String,
    private var authorities: Collection<GrantedAuthority>,
    private var attributes: Map<String, Any>?
) : OAuth2User, UserDetails {
    private var password: String? = null

    override fun getAttributes(): Map<String, Any> {
        return attributes!!
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return authorities
    }

    override fun getName(): String {
        return name
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setAuthorities(authorities: Collection<GrantedAuthority>) {
        this.authorities = authorities
    }

    fun setAttributes(attributes: Map<String, Any>?) {
        this.attributes = attributes
    }
}