package uz.pdp.appbank.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class Auditing implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            Staff staff = (Staff) authentication.getPrincipal();
            return Optional.of(staff.getId());
        }
        return Optional.empty();
    }
}
