package project.gizka.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User {
    private final AppUser user;

    public AuthUser(@NotNull AppUser user){
        super(user.getChat(),"{noop}password", user.getRoles());
        this.user = user;
    }

    public Long id(){
        return user.getId();
    }

    public AppUser getUser(){
        return user;
    }
}
