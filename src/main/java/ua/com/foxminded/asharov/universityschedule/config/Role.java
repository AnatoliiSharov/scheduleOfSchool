package ua.com.foxminded.asharov.universityschedule.config;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    CREATOR(Recall.CREATOR), MODERATOR(Recall.MODERATOR), VIEWER(Recall.VIEWER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Recall {

        public static final String CREATOR = "ROLE_CREATOR";
        public static final String MODERATOR = "ROLE_MODERATOR";
        public static final String VIEWER = "ROLE_VIEWER";

        private Recall() {
            throw new IllegalStateException("Recall class");
        }

    }
}
