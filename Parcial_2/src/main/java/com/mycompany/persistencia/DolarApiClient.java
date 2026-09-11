package com.mycompany.persistencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.modelo.Moneda;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DolarApiClient {

    private final MonedaRepository monedaRepository;
    private final RestTemplate restTemplate;

    public DolarApiClient(MonedaRepository monedaRepository) {
        this.monedaRepository = monedaRepository;
        this.restTemplate = new RestTemplate();
    }

    // Tarea programada: se ejecuta a los 2 segundos de iniciar y luego cada 10 minutos (600.000 ms)
    @Scheduled(fixedRate = 600000, initialDelay = 2000)
    public void sincronizacionAutomatica() {
        System.out.println(">>> EJECUTANDO SINCRONIZACIÓN AUTOMÁTICA EN SEGUNDO PLANO <<<");
        sincronizarCotizaciones();
    }

    public List<Moneda> sincronizarCotizaciones() {
        String url = "https://dolarapi.com/v1/dolares";
        DolarApiResponse[] respuesta = restTemplate.getForObject(url, DolarApiResponse[].class);
        List<Moneda> monedasGuardadas = new ArrayList<>();

        if (respuesta != null) {
            for (DolarApiResponse dto : respuesta) {
                String nombreFormat = "Dólar " + dto.nombre;

                Moneda moneda = monedaRepository.findByNombreIgnoreCase(nombreFormat)
                        .orElse(new Moneda());

                moneda.setNombre(nombreFormat);
                moneda.setCompra(String.valueOf(dto.compra));
                moneda.setVenta(String.valueOf(dto.venta));

                monedasGuardadas.add(monedaRepository.save(moneda));
            }
        }

        return monedasGuardadas;
    }

    private static class DolarApiResponse {
        @JsonProperty("nombre")
        public String nombre;

        @JsonProperty("compra")
        public Double compra;

        @JsonProperty("venta")
        public Double venta;
    }
}