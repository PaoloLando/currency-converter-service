package com.mycompany.controller;

import com.mycompany.modelo.Moneda;
import com.mycompany.persistencia.DolarApiClient;
import com.mycompany.persistencia.MonedaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonedaControllerTest {

    @Mock
    private MonedaRepository monedaRepository;

    @Mock
    private DolarApiClient dolarApiClient;

    @InjectMocks
    private MonedaController monedaController;

    private Moneda monedaBlue;

    @BeforeEach
    void setUp() {
        monedaBlue = new Moneda();
        monedaBlue.setId(1L);
        monedaBlue.setNombre("Dólar Blue");
        monedaBlue.setCompra("1500.0");
        monedaBlue.setVenta("1540.0");
    }

    @Test
    void convertirMonedaExitoso() {
        // Arrange (Simulación de comportamiento del repositorio)
        when(monedaRepository.findByNombreIgnoreCase("Dólar Blue"))
                .thenReturn(Optional.of(monedaBlue));

        // Act (Ejecución del método)
        ResponseEntity<?> respuesta = monedaController.convertirMoneda(154000.0, "blue", "venta");

        // Assert (Verificación de resultados)
        assertNotNull(respuesta);
        assertEquals(200, respuesta.getStatusCode().value());
        verify(monedaRepository, times(1)).findByNombreIgnoreCase("Dólar Blue");
    }

    @Test
    void convertirMonedaMontoInvalidoLanzaExcepcion() {
        // Act & Assert
        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> monedaController.convertirMoneda(-100.0, "blue", "venta")
        );

        assertEquals("El monto debe ser un número positivo mayor a cero.", excepcion.getMessage());
    }
}