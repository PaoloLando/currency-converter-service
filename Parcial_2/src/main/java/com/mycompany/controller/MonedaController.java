package com.mycompany.controller;

import com.mycompany.modelo.Moneda;
import com.mycompany.persistencia.DolarApiClient;
import com.mycompany.persistencia.MonedaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monedas")
public class MonedaController {

    private final MonedaRepository monedaRepository;
    private final DolarApiClient dolarApiClient;

    public MonedaController(MonedaRepository monedaRepository, DolarApiClient dolarApiClient) {
        this.monedaRepository = monedaRepository;
        this.dolarApiClient = dolarApiClient;
    }

    @GetMapping
    public List<Moneda> obtenerTodas() {
        return monedaRepository.findAll();
    }

    // POST /api/monedas/sincronizar -> Consume DolarApi y guarda en BBDD
    @PostMapping("/sincronizar")
    public ResponseEntity<List<Moneda>> sincronizar() {
        List<Moneda> resultado = dolarApiClient.sincronizarCotizaciones();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public Moneda crearMoneda(@RequestBody Moneda moneda) {
        return monedaRepository.save(moneda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMoneda(@PathVariable Long id) {
        if (monedaRepository.existsById(id)) {
            monedaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // GET /api/monedas/convertir?monto=100000&tipo=blue&operacion=compra
    // GET /api/monedas/convertir?monto=100000&tipo=blue&operacion=compra
    @GetMapping("/convertir")
    public ResponseEntity<?> convertirMoneda(
            @RequestParam Double monto,
            @RequestParam(defaultValue = "blue") String tipo,
            @RequestParam(defaultValue = "venta") String operacion) {

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser un número positivo mayor a cero.");
        }

        String tipoFormateado = tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();
        String nombreBuscar = "Dólar " + tipoFormateado;

        Moneda moneda = monedaRepository.findByNombreIgnoreCase(nombreBuscar)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de dólar no encontrado: " + tipo));

        double cotizacion;
        if (operacion.equalsIgnoreCase("compra")) {
            cotizacion = Double.parseDouble(moneda.getCompra());
        } else if (operacion.equalsIgnoreCase("venta")) {
            cotizacion = Double.parseDouble(moneda.getVenta());
        } else {
            throw new IllegalArgumentException("La operación debe ser 'compra' o 'venta'.");
        }

        double resultado = monto / cotizacion;

        return ResponseEntity.ok(java.util.Map.of(
                "montoPesos", monto,
                "tipoDolar", moneda.getNombre(),
                "cotizacionAplicada", cotizacion,
                "operacion", operacion.toLowerCase(),
                "resultadoDolares", Math.round(resultado * 100.0) / 100.0
        ));
    }
}