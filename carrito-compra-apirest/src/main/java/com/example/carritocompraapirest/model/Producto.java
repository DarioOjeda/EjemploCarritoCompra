package com.example.carritocompraapirest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Producto {
    private int id;
    private String nombre;
    private int precio;
}
