package veterinario.exercicio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import veterinario.exercicio.entities.Veterinario;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {

   public List<Veterinario> findByNomeContains(String nome);
}
