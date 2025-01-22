package com.espe.estudiantes;

import com.espe.estudiantes.controllers.EstudianteController;
import com.espe.estudiantes.models.entities.Estudiante;
import com.espe.estudiantes.services.EstudiantesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class EstudiantesApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudiantesService estudiantesService;

    @Test
    void testCrearEstudiante() throws Exception {
        // Crear un objeto Estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan");
        estudiante.setApellido("Perez");
        estudiante.setEmail("juan.perez@email.com");
        estudiante.setTelefono("1234567890");

        // Definir el comportamiento del mock de EstudiantesService
        Mockito.when(estudiantesService.save(Mockito.any(Estudiante.class)))
               .thenReturn(estudiante);


        // Realizar una solicitud POST al endpoint
        mockMvc.perform(post("/api/estudiantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"email\":\"juan.perez@email.com\",\"telefono\":\"1234567890\"}"))
                .andExpect(status().isCreated()) // Verificar que el estado HTTP sea 201 (Created)
                .andExpect(jsonPath("$.nombre").value("Juan")) // Verificar que el campo 'nombre' sea 'Juan'
                .andExpect(jsonPath("$.apellido").value("Perez")); // Verificar que el campo 'apellido' sea 'Perez'
    }

}
