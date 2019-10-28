<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
var urlBaseWeb = '<c:url value="/" />';
</script>

<script type="text/javascript">
	function loadCategorias(){
		var urlListarMarcas = urlBaseWeb + 'producto/listarCategorias';
		$.ajax({url : urlListarMarcas,
			dataType: "json",
			type 	: "GET",
			success : function(data) {
				if(data != null && data != ""){
					var items = [];
					$.each(data,function(key, val){
						items.push('<label for="chk_'+key+'"><input type="checkbox" name="categorias" value="'+val.idCategoria+'" id="chk_'+key+'"> '+val.nombre+'aa </label>');
					});
					
					$('#div_categorias').html(items.join(""));
					$("#btnBuscarProducto").trigger('click');
				}
			}
		});
	}
	
	function loadMarcas(){
		var urlListarMarcas = urlBaseWeb + 'producto/listarMarcas';
		$.ajax({url : urlListarMarcas,
			dataType: "json",
			type 	: "GET",
			success : function(data) {
				if(data != null && data != ""){
					var items = [];
					items.push('<option value=""> - Todas - </option>');
					$.each(data,function(key, val){
						items.push('<option value="'+val.idMarcaCatalogo+'">'+val.nombre+'sas</option>');
					});
					
					$('#cbo_marcas').html(items.join(""));
				}
			},
			error: function(){
				console.log('error');
			}
		});
	}
</script>

<script type="text/javascript">
$(document).ready(function(){

	loadMarcas();
	loadCategorias();
	
	$("#btnBuscarProducto").on("click",function(){
		$('#frmCatalogoProducto').submit();
	});

	$(document).on("click", ".pagination .item", function () {
		var numPageActual = $("input[name=numeroPagina]").val();
		var numPage = $(this).attr('numPage');
		if(numPage != numPageActual){
			$("input[name=numeroPagina]").val(numPage);
			console.log("click");
			$("#btnBuscarProducto").trigger('click');
		}
	});
	
	$('#frmCatalogoProducto').validate({
		submitHandler: function(form) {
			$(form).ajaxSubmit({
				success: function(responseText,statusText,xhr,$form){
					$('#div_resultado').html(responseText);
				}
			});
		}
	});
});
</script>
	<div id="publica">
		<div align="center">
			<table width="800" border="0">
				<tr>
					<td><form:form id="frmCatalogoProducto"
							action="${pageContext.request.contextPath}/producto/buscarProductos"
							method="POST" autocomplete="off"
							commandName="buscarProductosForm">

							<table border="0" width="100%" cellspacing="4" cellpadding="4"
								class="tabla">
								<tr>
									<td colspan="2">&nbsp;</td>
									<td align="right"><strong>Palabra clave / Nº de
											producto</strong></td>
									<td>&nbsp;</td>
								</tr>

								<tr>
									<td colspan="2">&nbsp;</td>
									<td align="center"><input type="text" id="nombreProducto"
										name="nombreProducto" style="width: 95%"></td>
									<td><input type="button" id="btnBuscarProducto"
										value=" Buscar "></td>
								</tr>

								<tr>
									<td colspan="4" align="left"><strong>Categoriasjjjj</strong></td>
								</tr>

								<tr>
									<td colspan="4">
										<div id="div_categorias"></div>
									</td>
								</tr>

								<tr>
									<td colspan="3"><strong>Marca</strong></td>
									<td><strong>Ordenar por</strong></td>
								</tr>

								<tr>
									<td colspan="3"><select id="cbo_marcas" name="marca"></select>
									</td>

									<td><select name="orderBy">
											<c:forEach var="item" items="${mapOrderBy}">
												<option value="${item.key}">${item.value}</option>
											</c:forEach>
									</select></td>
								</tr>
							</table>
							<input type="hidden" name="numeroPagina" value="1">
						</form:form> <br />
						<div id="div_resultado"></div></td>
				</tr>
			</table>
		</div>
	</div>