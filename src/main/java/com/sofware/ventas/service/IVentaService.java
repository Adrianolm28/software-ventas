package com.sofware.ventas.service;

import com.sofware.ventas.dto.VentaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVentaService {
    Page<VentaDto> listarVentas(Pageable pageable);
    VentaDto obtenerVentaPorId(String id);
    VentaDto registrarVenta(VentaDto ventaDto, String usernameVendedor);
    void cancelarVenta(String id);
}