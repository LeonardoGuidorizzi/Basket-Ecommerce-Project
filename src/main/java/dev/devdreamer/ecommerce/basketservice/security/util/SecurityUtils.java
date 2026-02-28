package dev.devdreamer.ecommerce.basketservice.security.util;

import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getAuthenticatedUserId() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
