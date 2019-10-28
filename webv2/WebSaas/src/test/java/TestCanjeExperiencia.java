import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.piscos.domain.Cliente;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes=ScopeConfiguration.class)
/*@ContextConfiguration(locations = {"classpath:spring/root-context.xml", 
		"classpath:spring/security-context.xml", 
		"classpath:hazelcast/hazelcast-bean.xml",
		"classpath:spring/servlet-context.xml"})

*/
//@WebAppConfiguration
public class TestCanjeExperiencia {
	
	private static Logger logger = LoggerFactory.getLogger(TestCanjeExperiencia.class);
	
	@Autowired
	private WebApplicationContext wac;
	 
	@Autowired
	protected MockServletContext mockServletContext;
	
	private MockMvc mockMvc;

	private MockHttpSession mocksession;
	 
	private String var1;
	  
	// @BeforeClass
	public static void setup1() {
		System.setProperty("propertiesHome", "D:\\server_gitlab\\configuration-saas\\LibrariesSaas\\src\\main\\resources\\properties");
	    // Instantiate your CsvConfig instance here if applicable.
	}
	 
	//@Before
	public void setup() {
		logger.info("SETTING MOCK");
		 
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
 	 
	// @Test
//	public void a_registrarPaso1() throws Exception {
//		var1 ="Registro pedido";
//		logger.info("REGISTRO PASO 1:" + var1);
//		mocksession = new MockHttpSession();
//
//		ReservaJoinnusOutput reserva = new ReservaJoinnusOutput();
//		EventoJoinnus eventoJoinnus = new EventoJoinnus();
//		eventoJoinnus.setIdEvento(1);
//		reserva.setEventoJoinnus(eventoJoinnus);
//		
//		EntradaDetalleOutput entradasJoinnus = new EntradaDetalleOutput();
//		entradasJoinnus.setIdEntrada("2");
//		entradasJoinnus.setEntradasSeleccionadas(3);
//		reserva.setEntradasJoinnus(entradasJoinnus);
//	
//		reserva.setApellidos("velasquez");
//		reserva.setNombres("erick");
//		reserva.setCorreo("erick@plazapoints.com");
//	 
//		logger.info("DATOS A ENVIAR:"+reserva);
//	  
//		this.mockMvc.perform(
//		    post("/experiencias/compra/registrarDatos").
//		    contentType(MediaType.APPLICATION_JSON).
//		    content(reserva.toString()).
//		    session(mocksession))
//		.andExpect(status().isOk())
//		.andExpect(jsonPath("estado", is(0)));	  
//	 }

	//@Test
	public void b_finalizarCanje() throws Exception {
		logger.info("FINALIZAR  CANJE PASO 2:"+var1);
		Cliente cliente = new Cliente();
		cliente.setIdCliente(3436);
		cliente.setNombre("Gino");
		cliente.setApellidos("Pogino");
		cliente.setTotalPuntos(6000);
	  
		mocksession.setAttribute("sessionCliente", cliente);
		 
		this.mockMvc.perform(
				get("/experiencias/compra/finalizarCanje")
				.session(mocksession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("estado", is(0)));	  
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
 
}
