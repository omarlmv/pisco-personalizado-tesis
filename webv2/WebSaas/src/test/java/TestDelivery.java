import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;

//@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@ContextConfiguration(locations = {
        "classpath:spring/root-context.xml",
        "classpath:spring/security-context.xml",
        "classpath:spring/servlet-context.xml"
        }
)
@TestPropertySource(properties = {
	    "propertiesHome=D:/server_gitlab/configuration-saas/LibrariesSaas/src/main/resources/properties",
	})
//@WebAppConfiguration("classpath:spring/servlet-context.xml")
public class TestDelivery {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	//@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	//@Test
	public void testHelloEndpointIsOK() throws Exception {
		this.mockMvc.perform(get("/terminos-y-condiciones/listar"))
	            .andExpect(status().isOk())
	            .andExpect(content().string("Hola Mundo"));
	}
	
	public void addCarrito() {
		CarritoCompra carrito = new CarritoCompra();

		List<CarritoDetalle> detalles = new ArrayList<>();
		CarritoDetalle det = new CarritoDetalle();
		
		CatalogoProducto catalogoProducto = new CatalogoProducto();
		
		det.setCatalogoProducto(catalogoProducto );
		
		carrito.setDetalles(detalles );
	}
	 MockHttpSession session = new MockHttpSession();
	//@Test
	public void addItemCart() throws Exception {
		
		 
		 MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/carritoProducto/agregarItem/dishonored-2-4755528660/1").
				 accept(MediaType.APPLICATION_JSON).session(session);
		 
//		this.mockMvc.perform(get("/carritoProducto/agregarItem/{keyItem}/1"))
//        .andExpect(status().isOk())
//        .andExpect(content().string("Hola Mundo"));
		
		 MvcResult result =  this.mockMvc.perform(builder).
				 andExpect(MockMvcResultMatchers.status().isOk()).
				 andReturn();
		 
		 String content = result.getResponse().getContentAsString();
		 System.out.println("contenido:"+content);
		//resultActions.andDo(MockMvcResultHandlers.print());
		listarCarritoItems();
		
	}
	
	public void listarCarritoItems() throws Exception {
			System.out.println("listando carrito");
		 ResultActions resultActions = this.mockMvc.perform(get("/carritoProducto/listar").session(session))
        .andExpect(status().isOk());
        //.andExpect(content().string("Hola Mundo"));
		
		 String content = resultActions.andReturn().getResponse().getContentAsString();
		 System.out.println("carrito lista:");
		 System.out.println(content);
		/*ResultActions result =
     this.mockMvc.perform(post(resource).sessionAttr(Constants.SESSION_USER, user).param("parameter", "parameterValue"))
        .andExpect(status().isOk());
String content = result.andReturn().getResponse().getContentAsString();
		 * */
		 listarDeliveryOpciones();
	}
	
	public void listarDeliveryOpciones() throws Exception {
		System.out.println("listarDeliveryOpciones");
		 int idUbigeoDistrito = 1396;
		 int idUbigeoProv = 1395;
		 int idUbigeoDep = 1394;
		 ResultActions resultActions = this.mockMvc.perform(
				 get("/carritoProducto/delivery/listarTiemposEntrega/"+idUbigeoDistrito+"/"+idUbigeoProv+"/"+idUbigeoDep).session(session)
				 )
			        .andExpect(status().isOk());
		 String content = resultActions.andReturn().getResponse().getContentAsString();
		 System.out.println("delivery opciones:"+content);
	}
	
}
