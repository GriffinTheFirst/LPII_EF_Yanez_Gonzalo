package pe.edu.cibertec.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pe.edu.cibertec.web.model.User;

public interface IUserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2")
	User findByUserAndPassword(String email, String password);
	
	@Query("SELECT u FROM User u WHERE u.registro >= CURRENT_DATE - 6 MONTH")
	List<User> consulta();
}
