package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.auth.*;
import cl.ayudarnos.api.entity.Organization;
import cl.ayudarnos.api.entity.User;
import cl.ayudarnos.api.entity.enums.Role;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.repository.OrganizationRepository;
import cl.ayudarnos.api.repository.UserRepository;
import cl.ayudarnos.api.security.JwtTokenProvider;
import cl.ayudarnos.api.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       OrganizationRepository organizationRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse registerDonor(RegisterDonorRequest request) {
        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("El correo ya se encuentra registrado");
        }

        User user = new User(
                request.getEmail().trim().toLowerCase(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getPhone(),
                Role.ROLE_DONOR
        );

        user = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                null,
                null
        );
    }

    @Transactional
    public AuthResponse registerOrganization(RegisterOrgRequest request) {
        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("El correo ya se encuentra registrado");
        }

        User user = new User(
                request.getEmail().trim().toLowerCase(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getPhone(),
                Role.ROLE_ORGANIZATION
        );

        user = userRepository.save(user);

        Organization organization = new Organization(
                user,
                request.getOrgName().trim(),
                request.getOrgType(),
                request.getRutTaxId(),
                (request.getFirstName() + " " + request.getLastName()).trim(),
                request.getPhone(),
                request.getRegion().trim(),
                request.getCommune().trim(),
                request.getAddress(),
                request.getDescription(),
                true // Auto-verified for development/demo ease
        );

        organization = organizationRepository.save(organization);
        user.setOrganization(organization);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                organization.getId(),
                organization.getName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                token,
                principal.getId(),
                principal.getUsername(),
                principal.getFullName(),
                Role.valueOf(principal.getAuthorities().iterator().next().getAuthority()),
                principal.getOrganizationId(),
                principal.getOrganizationName()
        );
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        UUID orgId = user.getOrganization() != null ? user.getOrganization().getId() : null;
        String orgName = user.getOrganization() != null ? user.getOrganization().getName() : null;

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                orgId,
                orgName
        );
    }
}
