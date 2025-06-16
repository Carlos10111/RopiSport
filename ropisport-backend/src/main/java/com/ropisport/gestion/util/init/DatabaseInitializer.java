package com.ropisport.gestion.util.init; // o com.ropisport.gestion.util.init

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ropisport.gestion.model.entity.Rol;
import com.ropisport.gestion.model.entity.Usuario;
import com.ropisport.gestion.repository.RolRepository;
import com.ropisport.gestion.repository.UsuarioRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear rol de administrador si no existe
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_ADMIN");
                    nuevoRol.setDescripcion("Administrador general con acceso completo");
                    return rolRepository.save(nuevoRol);
                });

        // Crear también el rol de administrador de socias
        Rol rolAdminSocias = rolRepository.findByNombre("ROLE_ADMIN_SOCIAS")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_ADMIN_SOCIAS");
                    nuevoRol.setDescripcion("Administrador de socias con permisos limitados");
                    return rolRepository.save(nuevoRol);
                });

        // Crear usuario administrador por defecto si no existe ningún usuario
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@ropisport.com");
            admin.setPasswordHash(passwordEncoder.encode("alvaro"));
            admin.setNombreCompleto("Administrador General");
            admin.setActivo(true);
            admin.setRol(rolAdmin);

            usuarioRepository.save(admin);

            System.out.println("Usuario administrador creado con éxito. Username: admin, Password: contraseña_inicial");
        }
    }
}