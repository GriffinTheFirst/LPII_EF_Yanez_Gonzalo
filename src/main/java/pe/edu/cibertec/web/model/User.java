package pe.edu.cibertec.web.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.persistence.*;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
@NamedQuery(name="User.remove", query="DELETE FROM User u WHERE u.iduser = :param")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "iduser")
	private Integer iduser;
	@Column(name = "name")
	private String name;
	private String apellido;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "nacimineto")
	private Date nacimineto;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@ManyToOne
	private Role role;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "registro")
	private Date registro;
	
	public User() {
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIduser() {
		return this.iduser;
	}

	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getNacimineto() {
		return this.nacimineto;
	}

	public void setNacimineto(Date nacimineto) {
		this.nacimineto = nacimineto;
	}
	
	public Date getRegistro() {
		return this.registro;
	}

	public void setRegistro(Date registro) {
		this.registro = registro;
	}
	@PrePersist
    public void prePersist() {
        this.registro =Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());  // Asigna la fecha actual automáticamente antes de guardar.
    }
	
	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
}