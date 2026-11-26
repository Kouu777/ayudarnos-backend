package cl.ayudarnos.api.data;

import cl.ayudarnos.api.entity.*;
import cl.ayudarnos.api.entity.enums.*;
import cl.ayudarnos.api.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final CatastropheRepository catastropheRepository;
    private final NeedRepository needRepository;
    private final VolunteerOpportunityRepository volunteerOpportunityRepository;
    private final UrgentAlertRepository urgentAlertRepository;
    private final GroupDonationRepository groupDonationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      OrganizationRepository organizationRepository,
                      CatastropheRepository catastropheRepository,
                      NeedRepository needRepository,
                      VolunteerOpportunityRepository volunteerOpportunityRepository,
                      UrgentAlertRepository urgentAlertRepository,
                      GroupDonationRepository groupDonationRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.catastropheRepository = catastropheRepository;
        this.needRepository = needRepository;
        this.volunteerOpportunityRepository = volunteerOpportunityRepository;
        this.urgentAlertRepository = urgentAlertRepository;
        this.groupDonationRepository = groupDonationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Base de datos ya cuenta con registros. Omitiendo precarga de datos.");
            return;
        }

        logger.info("Precargando datos iniciales de Ayudarnos.cl basados en los mockups...");

        // 1. Usuarios base
        User admin = new User(
                "admin@ayudarnos.cl",
                passwordEncoder.encode("admin123"),
                "Admin",
                "Ayudarnos",
                "+56 9 0000 0000",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        User donorPedro = new User(
                "pedro.jorquera@ejemplo.cl",
                passwordEncoder.encode("password123"),
                "Pedro",
                "Jorquera",
                "+56 9 1234 5678",
                Role.ROLE_DONOR
        );
        userRepository.save(donorPedro);

        User donorCamila = new User(
                "camila.gonzalez@ejemplo.cl",
                passwordEncoder.encode("password123"),
                "Camila",
                "González",
                "+56 9 8765 4321",
                Role.ROLE_DONOR
        );
        userRepository.save(donorCamila);

        // 2. Organizaciones y sus usuarios administradores
        // Municipalidad de Concepción
        User orgUserConcepcion = new User(
                "concepcion@ayudarnos.cl",
                passwordEncoder.encode("password123"),
                "Contacto",
                "Concepción",
                "+56 41 222 3333",
                Role.ROLE_ORGANIZATION
        );
        userRepository.save(orgUserConcepcion);

        Organization orgConcepcion = new Organization(
                orgUserConcepcion,
                "Municipalidad de Concepción",
                OrganizationType.MUNICIPALITY,
                "69.070.300-8",
                "DIDECO Concepción",
                "+56 41 222 3333",
                "Región del Biobío",
                "Concepción",
                "O'Higgins 525, Concepción",
                "Municipalidad de Concepción canalizando ayuda para afectados de la zona.",
                true
        );
        organizationRepository.save(orgConcepcion);
        orgUserConcepcion.setOrganization(orgConcepcion);

        // Bomberos de Chile, 3ª Compañía Quilpué
        User orgUserQuilpue = new User(
                "quilpue@bomberos.cl",
                passwordEncoder.encode("password123"),
                "Capitán",
                "Bomberos Quilpué",
                "+56 32 291 1132",
                Role.ROLE_ORGANIZATION
        );
        userRepository.save(orgUserQuilpue);

        Organization orgQuilpue = new Organization(
                orgUserQuilpue,
                "Bomberos de Chile, 3ª Compañía Quilpué",
                OrganizationType.FIREFIGHTERS,
                "70.017.400-K",
                "Tercera Compañía de Bomberos",
                "+56 32 291 1132",
                "Región de Valparaíso",
                "Quilpué",
                "Los Carrera 840, Quilpué",
                "Cuerpo de Bomberos canalizando insumos y víveres para familias evacuadas.",
                true
        );
        organizationRepository.save(orgQuilpue);
        orgUserQuilpue.setOrganization(orgQuilpue);

        // Cruz Roja Chilena, filial Temuco
        User orgUserTemuco = new User(
                "temuco@cruzroja.cl",
                passwordEncoder.encode("password123"),
                "Coordinador",
                "Cruz Roja Temuco",
                "+56 45 221 4455",
                Role.ROLE_ORGANIZATION
        );
        userRepository.save(orgUserTemuco);

        Organization orgTemuco = new Organization(
                orgUserTemuco,
                "Cruz Roja Chilena, filial Temuco",
                OrganizationType.RED_CROSS,
                "70.016.900-6",
                "Filial Temuco",
                "+56 45 221 4455",
                "Región de la Araucanía",
                "Temuco",
                "Manuel Montt 680, Temuco",
                "Organización humanitaria de ayuda en situaciones de emergencia y desastre.",
                true
        );
        organizationRepository.save(orgTemuco);
        orgUserTemuco.setOrganization(orgTemuco);

        // Fundación Un Techo para Chile
        User orgUserTecho = new User(
                "coquimbo@techo.cl",
                passwordEncoder.encode("password123"),
                "Encargado",
                "Techo Coquimbo",
                "+56 51 223 9988",
                Role.ROLE_ORGANIZATION
        );
        userRepository.save(orgUserTecho);

        Organization orgTecho = new Organization(
                orgUserTecho,
                "Fundación Un Techo para Chile",
                OrganizationType.FOUNDATION,
                "65.012.345-7",
                "Equipo Emergencias Techo",
                "+56 51 223 9988",
                "Región de Coquimbo",
                "Coquimbo",
                "Aldunate 1200, Coquimbo",
                "Fundación trabajando por el acceso a agua y soluciones de emergencia.",
                true
        );
        organizationRepository.save(orgTecho);
        orgUserTecho.setOrganization(orgTecho);

        // 3. Catástrofes activas
        Catastrophe catastrofeNacional = new Catastrophe(
                "Emergencias Climáticas e Incendios Chile 2026",
                "Coordinación nacional de asistencia a damnificados y comunidades evacuadas por catástrofes.",
                "Nacional",
                true
        );
        catastropheRepository.save(catastrofeNacional);

        // 4. Necesidades activas (Mockups)
        Need need1 = new Need(
                orgConcepcion,
                catastrofeNacional,
                "Kits de higiene niños",
                "Indica los productos de higiene y sus cantidades. La organización confirmará si puede recibir lo que ofreces.",
                "Higiene",
                "Región del Biobío",
                "Concepción",
                100,
                95,
                95,
                "Dona productos de higiene sellados, sin abrir y en buen estado. La organización confirmará si puede recibir lo que ofreces.",
                true
        );
        need1.setSuggestedProducts(Arrays.asList("Pañales para niños", "Alcohol gel sanitizante", "Jabón líquido", "Toallitas húmedas"));
        needRepository.save(need1);

        Need need2 = new Need(
                orgQuilpue,
                catastrofeNacional,
                "Alimentos no perecibles familias evacuadas",
                "Indica los alimentos no perecibles y sus cantidades. La organización confirmará si puede recibir lo que ofreces.",
                "Alimentos",
                "Región de Valparaíso",
                "Quilpué",
                500,
                200,
                40,
                "Dona alimentos no perecibles sellados, sin vencer y en buen estado. La organización confirmará si puede recibir lo que ofreces.",
                true
        );
        need2.setSuggestedProducts(Arrays.asList("Arroz integral", "Lentejas verdes", "Fideos", "Aceite vegetal", "Atún en lata"));
        needRepository.save(need2);

        Need need3 = new Need(
                orgTemuco,
                catastrofeNacional,
                "Frazadas, colchones familias",
                "Abrigo e insumos de descanso para familias albergadas o en viviendas de emergencia.",
                "Abrigo",
                "Región de la Araucanía",
                "Temuco",
                200,
                154,
                77,
                "Frazadas limpias o nuevas, colchones inflables o de espuma en óptimas condiciones.",
                false
        );
        need3.setSuggestedProducts(Arrays.asList("Frazadas térmicas", "Colchones 1 plaza", "Juegos de sábanas", "Almohadas"));
        needRepository.save(need3);

        Need need4 = new Need(
                orgTecho,
                catastrofeNacional,
                "Agua potable damnificados",
                "Agua potable para comunidades aisladas y familias que no cuentan con suministro hídrico activo.",
                "Agua",
                "Región de Coquimbo",
                "Coquimbo",
                1000,
                120,
                12,
                "Bidones sellados de agua purificada de 5L o 6L apta para consumo inmediato.",
                true
        );
        need4.setSuggestedProducts(Arrays.asList("Bidón de agua purificada 5L", "Bidón de agua purificada 6L", "Packs agua embotellada"));
        needRepository.save(need4);

        // 5. Módulo de Voluntariado precargado
        VolunteerOpportunity vol1 = new VolunteerOpportunity(
                orgConcepcion,
                catastrofeNacional,
                "Clasificación y embalaje en Centro de Acopio DIDECO",
                "Apoyo en recepción, clasificación por categorías y armado de kits de emergencia para familias albergadas.",
                "Gimnasio Municipal de Concepción",
                "Región del Biobío",
                "Concepción",
                "San Martín 450, Concepción",
                "Sábado y Domingo, turnos 09:00 - 14:00 y 14:00 - 19:00",
                "Mayor de 18 años, ropa cómoda y calzado cerrado para trabajo en bodega.",
                25,
                true
        );
        volunteerOpportunityRepository.save(vol1);

        VolunteerOpportunity vol2 = new VolunteerOpportunity(
                orgQuilpue,
                catastrofeNacional,
                "Descarga y orden de víveres en Cuartel de Bomberos",
                "Apoyo físico en descarga de camiones de ayuda humanitaria y distribución hacia puntos de acopio vecinales.",
                "Cuartel 3ª Compañía Quilpué",
                "Región de Valparaíso",
                "Quilpué",
                "Los Carrera 840, Quilpué",
                "Viernes y Sábado, 10:00 a 16:00",
                "Salud compatible con carga ligera/moderada, guantes de trabajo recomendados.",
                15,
                true
        );
        volunteerOpportunityRepository.save(vol2);

        // 6. Sistema de Alertas precargado
        UrgentAlert alert1 = new UrgentAlert(
                orgQuilpue,
                catastrofeNacional,
                "URGENTE: Se requieren con prioridad bidones de agua y alimentos listos para consumo",
                "Corte masivo de suministro en sector Los Pinos. Se necesita agua embotellada de forma inmediata.",
                AlertSeverity.CRITICAL,
                "Región de Valparaíso",
                "Quilpué"
        );
        urgentAlertRepository.save(alert1);

        // 7. Donación Grupal precargada
        GroupDonation group1 = new GroupDonation(
                "AYUD-GRP-2026-AROM1",
                need2,
                "Junta de Vecinos Los Aromos",
                GroupType.NEIGHBORHOOD_ASSOCIATION,
                donorPedro,
                "Pedro Jorquera",
                "pedro.jorquera@ejemplo.cl",
                "+56 9 1234 5678",
                "Campaña comunitaria: reunir alimentos entre 20 familias del pasaje",
                DeliveryMethod.DROP_OFF,
                "Quilpué",
                "Sábado por la mañana"
        );
        group1.addContribution(new GroupContributionItem("Familia Soto", "Arroz integral", 10, "Bolsas de 1 kg"));
        group1.addContribution(new GroupContributionItem("Vecina Carmen Depto 3", "Lentejas verdes", 8, "Bolsas de 1 kg"));
        groupDonationRepository.save(group1);

        logger.info("Precarga de datos de Ayudarnos.cl completada exitosamente.");
    }
}
