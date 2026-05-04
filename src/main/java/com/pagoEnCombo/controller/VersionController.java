package com.pagoEnCombo.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VersionController {

    // @Autowired es una opción, pero la inyección por constructor es la más recomendada
    private final BuildProperties buildProperties;

    // Inyección por constructor
    public VersionController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostMapping("/version")
    public String saludo() {
        return "Versión: " + buildProperties.getVersion();
    }
}
