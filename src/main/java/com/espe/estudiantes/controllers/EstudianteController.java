package com.espe.estudiantes.controllers;

import com.espe.estudiantes.models.entities.Estudiante;
import com.espe.estudiantes.services.EstudiantesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudiantesService service;

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Estudiante estudiante, BindingResult result) {
        // Verificar si hay errores en la validación
        if (result.hasErrors()) {
            // Crear un mapa para almacenar los mensajes de error
            Map<String, String> errores = new HashMap<>();

            // Recorrer los errores y agregarlos al mapa
            result.getFieldErrors().forEach(err -> {
                errores.put(err.getField(), err.getDefaultMessage());
            });

            // Retornar una respuesta con código 400 (Bad Request) y los errores
            return ResponseEntity.badRequest().body(errores);
        }

        // Guardar el estudiante y retornar una respuesta con código 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(estudiante));
    }


    @GetMapping
    public ResponseEntity<?> listar(){ return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Optional<Estudiante> estudianteOptional = service.findById(id);
        if (estudianteOptional.isPresent()){
            return ResponseEntity.ok().body(estudianteOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Estudiante estudiante, @PathVariable Long id){
        Optional<Estudiante> estudianteOptional = service.findById(id);
        if(estudianteOptional.isPresent()){
            Estudiante estudianteDB = estudianteOptional.get();
            estudianteDB.setNombre(estudiante.getNombre());
            estudianteDB.setApellido(estudiante.getApellido());
            estudianteDB.setEmail(estudiante.getEmail());
            estudianteDB.setFecha_nacimiento(estudiante.getFecha_nacimiento());
            estudianteDB.setTelefono(estudiante.getTelefono());
            estudianteDB.setCreado_en(estudiante.getCreado_en());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(estudianteDB));
        }
        return ResponseEntity.notFound().build();
    }

    //Eliminar un estudiante por ID

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Estudiante> estudiante = service.findById(id);
        if (estudiante.isPresent()){
            service.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado para eliminar");
        }
    }
}
