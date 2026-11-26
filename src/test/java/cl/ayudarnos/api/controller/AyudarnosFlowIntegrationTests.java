package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.alert.AlertSubscriptionRequest;
import cl.ayudarnos.api.dto.alert.AlertSubscriptionResponse;
import cl.ayudarnos.api.dto.alert.UrgentAlertResponse;
import cl.ayudarnos.api.dto.auth.AuthResponse;
import cl.ayudarnos.api.dto.auth.LoginRequest;
import cl.ayudarnos.api.dto.auth.RegisterDonorRequest;
import cl.ayudarnos.api.dto.auth.RegisterOrgRequest;
import cl.ayudarnos.api.dto.donation.*;
import cl.ayudarnos.api.dto.need.NeedResponse;
import cl.ayudarnos.api.dto.volunteer.VolunteerOpportunityResponse;
import cl.ayudarnos.api.dto.volunteer.VolunteerRegistrationRequest;
import cl.ayudarnos.api.dto.volunteer.VolunteerRegistrationResponse;
import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import cl.ayudarnos.api.entity.enums.GroupType;
import cl.ayudarnos.api.entity.enums.OrganizationType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AyudarnosFlowIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("1. Debe listar necesidades activas precargadas con porcentajes")
    void shouldListActiveNeeds() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/needs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<NeedResponse> list = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertNotNull(list);
        assertFalse(list.isEmpty());

        boolean hasHygiene = list.stream()
                .anyMatch(n -> n.getTitle().contains("higiene") && n.getProgressPercentage() == 95);
        assertTrue(hasHygiene);

        boolean hasAlimentos = list.stream()
                .anyMatch(n -> n.getTitle().contains("Alimentos") && n.getProgressPercentage() >= 40);
        assertTrue(hasAlimentos);
    }

    @Test
    @DisplayName("2. Debe registrar donante y permitir inicio de sesion")
    void shouldRegisterAndLoginDonor() throws Exception {
        String testEmail = "donor." + UUID.randomUUID() + "@ejemplo.cl";
        RegisterDonorRequest regReq = new RegisterDonorRequest(
                "Martina",
                "Rojas",
                testEmail,
                "password123",
                "password123",
                "+56 9 9988 7766"
        );

        MvcResult regRes = mockMvc.perform(post("/api/v1/auth/register/donor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authRes = objectMapper.readValue(regRes.getResponse().getContentAsString(), AuthResponse.class);
        assertNotNull(authRes.getToken());
        assertEquals("ROLE_DONOR", authRes.getRole().name());

        LoginRequest loginReq = new LoginRequest(testEmail, "password123");
        MvcResult loginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse loginAuth = objectMapper.readValue(loginRes.getResponse().getContentAsString(), AuthResponse.class);
        assertNotNull(loginAuth.getToken());
    }

    @Test
    @DisplayName("3. Debe registrar organizacion con rol ORGANIZATION")
    void shouldRegisterOrganization() throws Exception {
        String testEmail = "org." + UUID.randomUUID() + "@ayuda.cl";
        RegisterOrgRequest regReq = new RegisterOrgRequest();
        regReq.setFirstName("Marcos");
        regReq.setLastName("Vidal");
        regReq.setEmail(testEmail);
        regReq.setPassword("password123");
        regReq.setConfirmPassword("password123");
        regReq.setOrgName("ONG Solidaria Chile");
        regReq.setOrgType(OrganizationType.NGO);
        regReq.setRutTaxId("65.999.888-1");
        regReq.setPhone("+56 2 2333 4444");
        regReq.setRegion("Región Metropolitana");
        regReq.setCommune("Santiago");
        regReq.setAddress("Alameda 1234");
        regReq.setDescription("ONG de apoyo logístico.");

        MvcResult regRes = mockMvc.perform(post("/api/v1/auth/register/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authRes = objectMapper.readValue(regRes.getResponse().getContentAsString(), AuthResponse.class);
        assertEquals("ROLE_ORGANIZATION", authRes.getRole().name());
        assertNotNull(authRes.getOrganizationId());
    }

    @Test
    @DisplayName("4. Flujo de intencion de donacion: campos vacios de productos y perfil autocompletado en sesion")
    void shouldVerifyDonationFormTemplateAndPledgeFlow() throws Exception {
        MvcResult needsResult = mockMvc.perform(get("/api/v1/needs"))
                .andExpect(status().isOk())
                .andReturn();

        List<NeedResponse> needs = objectMapper.readValue(
                needsResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        NeedResponse target = needs.stream()
                .filter(n -> n.getOrganization().getName().contains("Concepción"))
                .findFirst()
                .orElse(needs.get(0));

        // 4.1 Formulario como invitado: productos vacíos y datos de perfil vacíos
        MvcResult guestFormRes = mockMvc.perform(get("/api/v1/donations/form-template/" + target.getId()))
                .andExpect(status().isOk())
                .andReturn();
        DonationFormTemplateDto guestTemplate = objectMapper.readValue(
                guestFormRes.getResponse().getContentAsString(),
                DonationFormTemplateDto.class
        );
        assertNotNull(guestTemplate.getItems());
        assertTrue(guestTemplate.getItems().isEmpty(), "Los productos deben iniciar vacíos");
        assertFalse(guestTemplate.isAuthenticated());
        assertEquals("", guestTemplate.getDonorFullName());

        // 4.2 Iniciar sesión como donante
        LoginRequest donorLogin = new LoginRequest("pedro.jorquera@ejemplo.cl", "password123");
        MvcResult donorLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(donorLogin)))
                .andExpect(status().isOk())
                .andReturn();
        AuthResponse donorAuth = objectMapper.readValue(donorLoginRes.getResponse().getContentAsString(), AuthResponse.class);

        // 4.3 Formulario con sesión: productos vacíos pero datos de perfil completados
        MvcResult authFormRes = mockMvc.perform(get("/api/v1/donations/form-template/" + target.getId())
                        .header("Authorization", "Bearer " + donorAuth.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        DonationFormTemplateDto authTemplate = objectMapper.readValue(
                authFormRes.getResponse().getContentAsString(),
                DonationFormTemplateDto.class
        );
        assertTrue(authTemplate.getItems().isEmpty(), "Los productos siempre deben iniciar vacíos");
        assertTrue(authTemplate.isAuthenticated());
        assertEquals("Pedro Jorquera", authTemplate.getDonorFullName());
        assertEquals("pedro.jorquera@ejemplo.cl", authTemplate.getDonorEmail());
        assertEquals("+56 9 1234 5678", authTemplate.getDonorPhone());

        // 4.4 Enviar donación con sesión (el donante solo ingresa lo que va a donar)
        DonationCreateRequest pledge = new DonationCreateRequest();
        pledge.setNeedId(target.getId());
        pledge.setDeliveryMethod(DeliveryMethod.DROP_OFF);
        pledge.setDonorCommune("Concepción");
        pledge.setAvailability("tardes, después de las 18:00");
        pledge.setConsentContactSharing(true);
        pledge.setItems(List.of(
                new DonationItemDto("Pañales para niños", 2, "Pack de 25 unidades"),
                new DonationItemDto("Alcohol gel sanitizante", 12, "Frasco de 100 ml")
        ));

        MvcResult pledgeResult = mockMvc.perform(post("/api/v1/donations")
                        .header("Authorization", "Bearer " + donorAuth.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pledge)))
                .andExpect(status().isCreated())
                .andReturn();

        DonationResponse donation = objectMapper.readValue(
                pledgeResult.getResponse().getContentAsString(),
                DonationResponse.class
        );
        assertNotNull(donation.getTrackingCode());
        assertEquals("Pedro Jorquera", donation.getDonorFullName());
        assertEquals("pedro.jorquera@ejemplo.cl", donation.getDonorEmail());
        assertEquals(DonationStatus.PLEDGED, donation.getStatus());

        // 4.5 Rastrear donación
        MvcResult trackResult = mockMvc.perform(get("/api/v1/donations/track/" + donation.getTrackingCode()))
                .andExpect(status().isOk())
                .andReturn();
        DonationResponse tracked = objectMapper.readValue(
                trackResult.getResponse().getContentAsString(),
                DonationResponse.class
        );
        assertEquals(donation.getId(), tracked.getId());

        // 4.6 Actualizar estado por la organización dueña
        LoginRequest orgLogin = new LoginRequest("concepcion@ayudarnos.cl", "password123");
        MvcResult orgLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgLogin)))
                .andExpect(status().isOk())
                .andReturn();
        AuthResponse orgAuth = objectMapper.readValue(orgLoginRes.getResponse().getContentAsString(), AuthResponse.class);

        DonationStatusUpdateRequest statusUpdate = new DonationStatusUpdateRequest(
                DonationStatus.COORDINATING,
                "Coordinando entrega presencial en punto DIDECO."
        );
        MvcResult updateResult = mockMvc.perform(patch("/api/v1/donations/" + donation.getId() + "/status")
                        .header("Authorization", "Bearer " + orgAuth.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andReturn();

        DonationResponse updated = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                DonationResponse.class
        );
        assertEquals(DonationStatus.COORDINATING, updated.getStatus());
    }

    @Test
    @DisplayName("5. Módulo de Voluntariado: listar convocatorias e inscribirse")
    void shouldListAndRegisterForVolunteering() throws Exception {
        // Listar convocatorias
        MvcResult oppResult = mockMvc.perform(get("/api/v1/volunteering"))
                .andExpect(status().isOk())
                .andReturn();
        List<VolunteerOpportunityResponse> opps = objectMapper.readValue(
                oppResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertNotNull(opps);
        assertFalse(opps.isEmpty());
        VolunteerOpportunityResponse opp = opps.get(0);

        // Inscribirse como voluntario
        VolunteerRegistrationRequest regReq = new VolunteerRegistrationRequest();
        regReq.setFullName("Ignacio Silva");
        regReq.setEmail("ignacio.silva@ejemplo.cl");
        regReq.setPhone("+56 9 7777 8888");
        regReq.setAvailabilityNotes("Disponible para clasificar insumos el sábado en la mañana");

        MvcResult regResult = mockMvc.perform(post("/api/v1/volunteering/" + opp.getId() + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        VolunteerRegistrationResponse regRes = objectMapper.readValue(
                regResult.getResponse().getContentAsString(),
                VolunteerRegistrationResponse.class
        );
        assertNotNull(regRes.getRegistrationCode());
        assertTrue(regRes.getRegistrationCode().startsWith("VOL-"));
        assertEquals("Ignacio Silva", regRes.getFullName());
    }

    @Test
    @DisplayName("6. Sistema de Alertas: consultar alertas activas y suscribirse a una zona")
    void shouldCheckActiveAlertsAndSubscribe() throws Exception {
        // Consultar alertas críticas activas
        MvcResult alertsRes = mockMvc.perform(get("/api/v1/alerts/active"))
                .andExpect(status().isOk())
                .andReturn();
        List<UrgentAlertResponse> alerts = objectMapper.readValue(
                alertsRes.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertNotNull(alerts);
        assertFalse(alerts.isEmpty());
        assertTrue(alerts.stream().anyMatch(a -> a.getTitle().contains("URGENTE")));

        // Suscribirse a alertas de Valparaíso
        AlertSubscriptionRequest subReq = new AlertSubscriptionRequest();
        subReq.setEmail("vecino.quilpue@ejemplo.cl");
        subReq.setPhone("+56 9 5555 4444");
        subReq.setRegion("Región de Valparaíso");
        subReq.setCommune("Quilpué");
        subReq.setCategory("ALIMENTOS");

        MvcResult subResult = mockMvc.perform(post("/api/v1/alerts/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subReq)))
                .andExpect(status().isCreated())
                .andReturn();

        AlertSubscriptionResponse subRes = objectMapper.readValue(
                subResult.getResponse().getContentAsString(),
                AlertSubscriptionResponse.class
        );
        assertNotNull(subRes.getId());
        assertTrue(subRes.isActive());
        assertEquals("Región de Valparaíso", subRes.getRegion());
    }

    @Test
    @DisplayName("7. Donaciones Grupales: crear iniciativa colectiva y sumar aportes vecinales")
    void shouldCreateGroupDonationAndAddContributions() throws Exception {
        MvcResult needsResult = mockMvc.perform(get("/api/v1/needs"))
                .andExpect(status().isOk())
                .andReturn();
        List<NeedResponse> needs = objectMapper.readValue(
                needsResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        NeedResponse need = needs.get(0);

        // Crear campaña grupal (Junta de Vecinos)
        GroupDonationCreateRequest groupReq = new GroupDonationCreateRequest();
        groupReq.setNeedId(need.getId());
        groupReq.setGroupName("Junta de Vecinos Los Aromos");
        groupReq.setGroupType(GroupType.NEIGHBORHOOD_ASSOCIATION);
        groupReq.setCoordinatorName("Gonzalo Tapia");
        groupReq.setCoordinatorEmail("gonzalo.tapia@juntadevecinos.cl");
        groupReq.setCoordinatorPhone("+56 9 6666 7777");
        groupReq.setTargetDescription("Meta comunitaria: 80 kg de legumbres y alimentos");
        groupReq.setDeliveryMethod(DeliveryMethod.DROP_OFF);
        groupReq.setDonorCommune("Concepción");
        groupReq.setAvailability("Sábado en la mañana");
        groupReq.setInitialContributions(List.of(
                new GroupContributionItemDto("Familia Muñoz (Casa 12)", "Lentejas verdes", 10, "Bolsas de 1 kg")
        ));

        MvcResult createRes = mockMvc.perform(post("/api/v1/donations/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupReq)))
                .andExpect(status().isCreated())
                .andReturn();

        GroupDonationResponse groupRes = objectMapper.readValue(
                createRes.getResponse().getContentAsString(),
                GroupDonationResponse.class
        );
        assertNotNull(groupRes.getTrackingCode());
        assertTrue(groupRes.getTrackingCode().startsWith("AYUD-GRP-"));
        assertEquals("Junta de Vecinos Los Aromos", groupRes.getGroupName());
        assertEquals(1, groupRes.getContributions().size());

        // Sumar un aporte de otro vecino
        GroupContributionItemDto neighborContribution = new GroupContributionItemDto(
                "Familia Vargas (Casa 18)",
                "Arroz integral",
                15,
                "Bolsas de 1 kg"
        );

        MvcResult addRes = mockMvc.perform(post("/api/v1/donations/group/" + groupRes.getId() + "/contributions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(neighborContribution)))
                .andExpect(status().isOk())
                .andReturn();

        GroupDonationResponse updatedGroup = objectMapper.readValue(
                addRes.getResponse().getContentAsString(),
                GroupDonationResponse.class
        );
        assertEquals(2, updatedGroup.getContributions().size());
        assertEquals(25, updatedGroup.getTotalItemsCount());

        // Rastrear la campaña grupal pública con su código
        MvcResult trackRes = mockMvc.perform(get("/api/v1/donations/group/track/" + groupRes.getTrackingCode()))
                .andExpect(status().isOk())
                .andReturn();
        GroupDonationResponse trackedGroup = objectMapper.readValue(
                trackRes.getResponse().getContentAsString(),
                GroupDonationResponse.class
        );
        assertEquals(groupRes.getId(), trackedGroup.getId());
        assertEquals(25, trackedGroup.getTotalItemsCount());
    }
}
