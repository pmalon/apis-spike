package com.phorest.spikes.apiserver.auth

import org.springframework.security.core.GrantedAuthority

data class User(val username: String, val authorities: Collection<GrantedAuthority>)