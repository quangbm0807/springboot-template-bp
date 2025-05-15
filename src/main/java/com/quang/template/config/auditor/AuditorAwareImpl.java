package com.quang.template.config.auditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    private static final Logger log = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            log.debug("No authenticated user found, using SYSTEM as auditor");
            return Optional.of("SYSTEM");
        }

        if (auth.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) auth.getPrincipal()).getUsername();
            log.debug("Using authenticated user as auditor: {}", username);
            return Optional.of(username);
        } else if (auth.getPrincipal() instanceof String) {
            log.debug("Using principal string as auditor: {}", auth.getPrincipal());
            return Optional.of(auth.getPrincipal().toString());
        } else {
            log.debug("Using auth name as auditor: {}", auth.getName());
            return Optional.of(auth.getName());
        }
    }
}