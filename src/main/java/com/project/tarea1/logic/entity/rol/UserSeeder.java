package com.project.tarea1.logic.entity.rol;

import com.project.tarea1.logic.entity.rol.Role;
import com.project.tarea1.logic.entity.rol.RoleEnum;
import com.project.tarea1.logic.entity.rol.RoleRepository;
import com.project.tarea1.logic.entity.user.User;
import com.project.tarea1.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createStandardUser();
    }

    private void createStandardUser() {
        String userEmail = "user@gmail.com";
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        User user = new User();
        user.setName("Regular");
        user.setLastname("User");
        user.setEmail(userEmail);
        user.setPassword(passwordEncoder.encode("user12345"));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
