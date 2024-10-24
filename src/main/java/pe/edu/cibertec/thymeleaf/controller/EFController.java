package pe.edu.cibertec.thymeleaf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.edu.cibertec.web.bd.MySQLDataSource;
import pe.edu.cibertec.web.model.User;
import pe.edu.cibertec.web.repository.IRoleRepository;
import pe.edu.cibertec.web.service.UserService;

@Controller
public class EFController {
	public static String sesionVar;
	
	@Autowired
	private IRoleRepository roleRepo;
	
	@Autowired
	private UserService userService;
	
	@GetMapping({"/","/login"})
	public String login(Model model) {
		System.out.println("Cargando login");
		User Unew = new User();
		model.addAttribute("userLogin", Unew);
		return "login";
	}
	@PostMapping("/login")
	public String login(@ModelAttribute User user, Model model) {
		System.out.println("Validando login");		
		String redirect = "login";
		User u = userService.validateUserByNameAndPassword(user.getEmail(), user.getPassword());
		if (u != null) {
			sesionVar = u.getName();
			return "redirect:/index";
		} else {
			model.addAttribute("errors", "Usuario o clave incorrectos");
			model.addAttribute("userLogin", new User());
		}
		return redirect;
	}
	
	@GetMapping("/cerrarSesion")
	public String logout(Model model) {
		System.out.println("Cerrando la sesión");
		model.addAttribute("userLogin", null);
		sesionVar = null;
		return "redirect:/login";
	}
	
	@GetMapping("/index")
	public String index(Model model) {
		if(sesionVar == null) {
			return "redirect:/login";
		} else {
			try {
				model.addAttribute("listaUsuario", userService.consulta());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "index";
		}
	}
	
	@GetMapping("/insertUsu")
	public String insertUsu(Model model) {
		model.addAttribute("crearUsu", new User());
		model.addAttribute("ltsRole", roleRepo.findAll());
		return "insertUsu";
	}
	@PostMapping("/insertUsu")
	public String insertUsu(@ModelAttribute User user, Model model) {
		userService.guardarUsuario(user);
		model.addAttribute("msj", "Inserción realizada");
		insertUsu(model);
		return "insertUsu";
	}
	
	@GetMapping("/actUsu/{iduser}")
	public String actUsu(@PathVariable  String iduser, Model model) {
		model.addAttribute("updUsu", new User());
		model.addAttribute("ltsRole", roleRepo.findAll());
		User usu = userService.buscarUsuario(iduser);
		model.addAttribute("updUsu", usu);
		return "actUsu";
	}
	@PostMapping("/actUsu")
	public String actUsu(@ModelAttribute User user, Model model) {
		userService.guardarUsuario(user);
		return "redirect:/index";
	}
	
	@GetMapping("/eliminarUsuario/{iduser}")
	public String eliminarUsuario(@PathVariable String iduser, Model model) {
		System.out.println("Ejecutando eliminar usaurio " + iduser);
		userService.eliminarUsuario(iduser);
		return "redirect:/index";
	}
	
	@RequestMapping(value = "/usuarioReporte", method = RequestMethod.GET)
	public void personaReporte(HttpServletResponse response) throws JRException, IOException {
		System.out.println("Generando reporte de usuarios");
		InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/Leaf_Grey.jasper");
		System.out.println(jasperStream);
		Map<String, Object> params = new HashMap<String, Object>();
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, MySQLDataSource.getMySQLConnection());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=reporte-usaurios.pdf");
		final OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}
}
