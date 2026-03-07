<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Carrito" />
</jsp:include>

<div class="container-main">
    <div class="cart-container">
        <h2 class="cart-title">Gestión del carrito</h2>

        <c:choose>
            <c:when test="${empty sessionScope.carrito.lineas}">
                <div class="alert-empty">
                    <h3>El carrito está vacío</h3>
                    <p>¡Añade productos para verlos aquí!</p>
                    <br>
                    <form action="${contexto}/FrontController" method="post">
                        <input type="hidden" name="accion" value="inicio">
                        <button type="submit" class="btn-primary">Ir al Catálogo</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>

                <div class="cart-layout">

                    <div class="cart-table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Producto</th>
                                    <th>Precio Unit.</th>
                                    <th>Cantidad</th>
                                    <th>Subtotal</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="total" value="0" />
                                <c:forEach var="linea" items="${sessionScope.carrito.lineas}">
                                    <c:set var="subtotal" value="${linea.producto.precio * linea.cantidad}" />
                                    <c:set var="total" value="${total + subtotal}" />

                                    <tr id="row-${linea.producto.idProducto}">
                                        <td>
                                            <div style="display:flex; align-items:center; gap:15px;">
                                                <img src="${contexto}/imagen/productos/${linea.producto.imagen}.jpg"
                                                     style="width:60px; height:60px; object-fit:contain; background:#fff; padding:5px; border-radius:4px;"
                                                     onerror="this.src='${contexto}/imagen/productos/default.jpg'">
                                                <div>
                                                    <strong style="font-size:1.1rem;">${linea.producto.nombre}</strong>
                                                    <div style="font-size:0.8rem; color:#aaa;">${linea.producto.marca}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€"/>
                                        </td>
                                        <td>
                                            <div style="display:flex; align-items:center; gap:5px;">
                                                <button type="button" class="qty-btn" onclick="actualizarCantidad(${linea.producto.idProducto}, -1)">-</button>
                                                <span id="qty-${linea.producto.idProducto}" class="qty-input">${linea.cantidad}</span>
                                                <button type="button" class="qty-btn" onclick="actualizarCantidad(${linea.producto.idProducto}, 1)">+</button>
                                            </div>
                                        </td>
                                        <td style="font-weight:bold; color:var(--header-footer-bg);">
                                            <span id="subtotal-${linea.producto.idProducto}">
                                                <fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </td>
                                        <td>
                                            <form action="${contexto}/PedidoController" method="post">

                                                <input type="hidden" name="accion" value="eliminarLinea">
                                                <input type="hidden" name="id" value="${linea.producto.idProducto}">
                                                <button type="submit" class="btn-link" style="color:var(--btn-danger);" title="Eliminar producto">
                                                    <i class="fas fa-trash-alt fa-lg"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div style="margin-top:20px; text-align:left;">
                            <form action="${contexto}/PedidoController" method="post">
                                <input type="hidden" name="accion" value="vaciarCarrito">
                                <button type="submit" class="btn-danger-outline" style="padding:10px 20px;">
                                    <i class="fas fa-trash"></i> Vaciar Carrito
                                </button>
                            </form>
                        </div>
                    </div>

                    <div class="cart-summary-wrapper">
                        <div class="cart-summary-box">
                            <h3 style="border-bottom:1px solid #444; padding-bottom:10px; margin-bottom:15px;">Resumen</h3>

                            <c:set var="base" value="${total / 1.21}" />
                            <c:set var="iva" value="${total - base}" />

                            <div class="summary-row">
                                <span>Base Imponible</span>
                                <span id="cart-base"><fmt:formatNumber value="${base}" type="currency" currencySymbol="€"/></span>
                            </div>
                            <div class="summary-row">
                                <span>IVA (21%)</span>
                                <span id="cart-iva"><fmt:formatNumber value="${iva}" type="currency" currencySymbol="€"/></span>
                            </div>

                            <div class="summary-total" style="font-size:1.5rem; margin-top:20px; color:var(--header-footer-bg);">
                                <span>TOTAL</span>
                                <span id="cart-total"><fmt:formatNumber value="${total}" type="currency" currencySymbol="€"/></span>
                            </div>

                            <div class="cart-actions" style="margin-top:20px;">

                                <c:choose>
                                    <c:when test="${not empty sessionScope.usuario}">
                                        <%-- Registrado --%>
                                        <form action="${contexto}/PedidoController" method="post">

                                            <input type="hidden" name="accion" value="comprar">
                                            <button type="submit" class="btn-buy-now btn-block">
                                                PAGAR AHORA <i class="fas fa-credit-card"></i>
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- Anónimo --%>
                                        <div style="text-align:center; margin-bottom:10px; color:var(--btn-danger); font-weight:bold; font-size:0.9rem; padding:10px; background:rgba(220, 53, 69, 0.1); border-radius:4px;">
                                            <i class="fas fa-lock"></i> No tienes cuenta creada
                                        </div>

                                        <form action="${contexto}/UsuarioController" method="post">
                                            <input type="hidden" name="accion" value="registro">
                                            <button type="submit" class="btn-primary btn-block" style="background-color: #28a745; border-color:#28a745;">
                                                <i class="fas fa-user-plus"></i> Regístrate aquí para comprar
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>

                                <form action="${contexto}/FrontController" method="post" style="margin-top:10px;">
                                    <input type="hidden" name="accion" value="inicio">
                                    <button type="submit" class="btn-link" style="color:#aaa; width:100%; justify-content:center;">
                                        Seguir comprando
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div> </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    const CONTEXT_PATH = '${contexto}';
</script>
<script src="${contexto}/JS/carrito.js"></script>

<jsp:include page="/INC/pie.jsp" />