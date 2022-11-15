package cl.nessfit.web.service;

import cl.nessfit.web.model.Instalacion;
import cl.nessfit.web.repository.IInstalacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InstalacionServiceTest {
    @Mock
    private IInstalacionRepository instalacionRepository;

    @InjectMocks
    private InstalacionService instalacionService;
    private Instalacion instalacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instalacion = new Instalacion();
        instalacion.setId("12345");
        instalacion.setNombre("Cancha 1");
        instalacion.setCosto(2000);
        instalacion.setDireccion("La reina 525");
        instalacion.setTipo("cancha");
        instalacion.setEstado(1);
    }
    @Test
    void verTodasIntalaciones() {
        when(instalacionRepository.findAll()).thenReturn(Arrays.asList(instalacion));
        assertNotNull(instalacionService.verTodasIntalaciones());
    }
}