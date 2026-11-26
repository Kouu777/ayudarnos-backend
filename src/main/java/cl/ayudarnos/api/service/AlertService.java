package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.alert.*;
import cl.ayudarnos.api.entity.AlertSubscription;
import cl.ayudarnos.api.entity.Catastrophe;
import cl.ayudarnos.api.entity.Organization;
import cl.ayudarnos.api.entity.UrgentAlert;
import cl.ayudarnos.api.entity.User;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.AlertSubscriptionRepository;
import cl.ayudarnos.api.repository.CatastropheRepository;
import cl.ayudarnos.api.repository.OrganizationRepository;
import cl.ayudarnos.api.repository.UrgentAlertRepository;
import cl.ayudarnos.api.repository.UserRepository;
import cl.ayudarnos.api.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    private final AlertSubscriptionRepository subscriptionRepository;
    private final UrgentAlertRepository alertRepository;
    private final OrganizationRepository organizationRepository;
    private final CatastropheRepository catastropheRepository;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    public AlertService(AlertSubscriptionRepository subscriptionRepository,
                        UrgentAlertRepository alertRepository,
                        OrganizationRepository organizationRepository,
                        CatastropheRepository catastropheRepository,
                        UserRepository userRepository,
                        OrganizationService organizationService) {
        this.subscriptionRepository = subscriptionRepository;
        this.alertRepository = alertRepository;
        this.organizationRepository = organizationRepository;
        this.catastropheRepository = catastropheRepository;
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    @Transactional
    public AlertSubscriptionResponse subscribe(AlertSubscriptionRequest request, UserPrincipal principal) {
        User user = null;
        String email;
        String phone;

        if (principal != null) {
            user = userRepository.findById(principal.getId()).orElse(null);
            email = (request.getEmail() != null && !request.getEmail().isBlank())
                    ? request.getEmail().trim().toLowerCase()
                    : (user != null ? user.getEmail() : principal.getUsername());

            phone = (request.getPhone() != null && !request.getPhone().isBlank())
                    ? request.getPhone().trim()
                    : (user != null && user.getPhone() != null ? user.getPhone() : "");
        } else {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new BadRequestException("El correo electrónico es obligatorio para suscribirse a alertas");
            }
            email = request.getEmail().trim().toLowerCase();
            phone = request.getPhone() != null ? request.getPhone().trim() : null;
        }

        AlertSubscription subscription = new AlertSubscription(
                user,
                email,
                phone,
                request.getRegion().trim(),
                request.getCommune() != null ? request.getCommune().trim() : null,
                request.getCategory()
        );

        subscription = subscriptionRepository.save(subscription);
        return mapSubscriptionToDto(subscription);
    }

    @Transactional(readOnly = true)
    public List<AlertSubscriptionResponse> getMySubscriptions(UserPrincipal principal) {
        return subscriptionRepository.findByUserIdAndActiveTrue(principal.getId()).stream()
                .map(this::mapSubscriptionToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unsubscribe(UUID subscriptionId, UserPrincipal principal) {
        AlertSubscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada"));

        if (principal != null && sub.getUser() != null && !sub.getUser().getId().equals(principal.getId())) {
            throw new BadRequestException("No tiene permisos para cancelar esta suscripción");
        }

        sub.setActive(false);
        subscriptionRepository.save(sub);
    }

    @Transactional(readOnly = true)
    public List<UrgentAlertResponse> getActiveAlerts(String region) {
        List<UrgentAlert> alerts = (region != null && !region.isBlank())
                ? alertRepository.findActiveByRegion(region.trim())
                : alertRepository.findByActiveTrueOrderByCreatedAtDesc();

        return alerts.stream().map(this::mapAlertToDto).collect(Collectors.toList());
    }

    @Transactional
    public UrgentAlertResponse publishAlert(UrgentAlertCreateRequest request, UserPrincipal principal) {
        if (principal.getOrganizationId() == null) {
            throw new BadRequestException("Solo organizaciones pueden emitir alertas urgentes");
        }

        Organization org = organizationRepository.findById(principal.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada"));

        Catastrophe catastrophe = null;
        if (request.getCatastropheId() != null) {
            catastrophe = catastropheRepository.findById(request.getCatastropheId())
                    .orElseThrow(() -> new ResourceNotFoundException("Catástrofe no encontrada"));
        }

        UrgentAlert alert = new UrgentAlert(
                org,
                catastrophe,
                request.getTitle().trim(),
                request.getMessage().trim(),
                request.getSeverity(),
                request.getRegion().trim(),
                request.getCommune() != null ? request.getCommune().trim() : null
        );

        alert = alertRepository.save(alert);

        // Simulación del despacho en tiempo real a los suscriptores de la zona
        long recipientCount = subscriptionRepository.findByActiveTrue().stream()
                .filter(sub -> "Todas".equalsIgnoreCase(sub.getRegion()) || sub.getRegion().equalsIgnoreCase(alert.getRegion()))
                .count();

        logger.info("ALERTA CRÍTICA EMITIDA: [{}] - '{}' en {}. Notificando a {} ciudadanos suscritos.",
                alert.getSeverity(), alert.getTitle(), alert.getRegion(), recipientCount);

        return mapAlertToDto(alert);
    }

    public AlertSubscriptionResponse mapSubscriptionToDto(AlertSubscription sub) {
        AlertSubscriptionResponse res = new AlertSubscriptionResponse();
        res.setId(sub.getId());
        res.setEmail(sub.getEmail());
        res.setPhone(sub.getPhone());
        res.setRegion(sub.getRegion());
        res.setCommune(sub.getCommune());
        res.setCategory(sub.getCategory());
        res.setActive(sub.isActive());
        res.setCreatedAt(sub.getCreatedAt());
        return res;
    }

    public UrgentAlertResponse mapAlertToDto(UrgentAlert alert) {
        UrgentAlertResponse res = new UrgentAlertResponse();
        res.setId(alert.getId());
        res.setTitle(alert.getTitle());
        res.setMessage(alert.getMessage());
        res.setSeverity(alert.getSeverity());
        res.setRegion(alert.getRegion());
        res.setCommune(alert.getCommune());
        res.setActive(alert.isActive());
        res.setOrganization(organizationService.mapToDto(alert.getOrganization()));
        if (alert.getCatastrophe() != null) {
            res.setCatastropheId(alert.getCatastrophe().getId());
            res.setCatastropheName(alert.getCatastrophe().getName());
        }
        res.setCreatedAt(alert.getCreatedAt());
        return res;
    }
}
