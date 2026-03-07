<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Factura de Compra" />
</jsp:include>

<div class="container-main">
    <div class="cart-container factura-wrapper">

        <div class="factura-header">
            <div>
                <h1 class="factura-title">¡PEDIDO REALIZADO CON ÉXITO!</h1>
                <p class="factura-subtitle">Desglose de la factura</p>
            </div>
            <div class="factura-date-box">
                <strong>Estado:</strong> Pagado y Finalizado
            </div>
        </div>

        <div class="factura-client-info">
            <h3 class="factura-client-title">Datos de facturación:</h3>
            <strong>${sessionScope.usuario.nombre} ${sessionScope.usuario.apellidos}</strong><br>
            NIF: ${sessionScope.usuario.nif}<br>
            ${sessionScope.usuario.direccion}<br>
            ${sessionScope.usuario.codigoPostal}, ${sessionScope.usuario.localidad} (${sessionScope.usuario.provincia})<br>
            Email: ${sessionScope.usuario.email}
        </div>

        <table class="factura-table" style="width: 100%; border-collapse: collapse; margin-bottom: 30px; text-align: left;">
            <thead>
                <tr style="background-color: #f8f9fa; border-bottom: 2px solid #ddd;">
                    <th style="padding: 12px; width: 60%;">Producto Comprado</th>
                    <th style="padding: 12px; text-align: center;">Cantidad</th>
                    <th style="padding: 12px; text-align: right;">Precio Unitario</th>
                    <th style="padding: 12px; text-align: right;">Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="linea" items="${factura.lineas}">
                    <tr style="border-bottom: 1px solid #eee;">

                        <%-- imagen, nombre y marca --%>
                        <td style="padding: 12px;">
                            <div style="display: flex; align-items: center; gap: 15px;">
                                <img src="${contexto}/imagen/productos/${linea.producto.imagen}.jpg" 
                                     alt="${linea.producto.nombre}" 
                                     style="width: 60px; height: 60px; object-fit: contain; background: white; padding: 2px; border: 1px solid #ccc; border-radius: 4px;"
                                     onerror="this.src='${contexto}/imagen/productos/default.jpg'">
                                <div>
                                    <strong style="font-size: 1.1rem; color: #333; display: block;">${linea.producto.nombre}</strong>
                                    <span style="font-size: 0.85rem; color: #333; text-transform: uppercase;">${linea.producto.marca}</span>
                                </div>
                            </div>
                        </td>

                        <%-- cantidad --%>
                        <td style="padding: 12px; text-align: center; vertical-align: middle; font-size: 1.1rem; font-weight: bold; color: #007bff;">
                            x ${linea.cantidad}
                        </td>

                        <%-- precio por unidad --%>
                        <td style="padding: 12px; text-align: right; vertical-align: middle; color: #555;">
                            <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€"/>
                        </td>

                        <%-- subototal --%>
                        <td style="padding: 12px; text-align: right; vertical-align: middle; font-weight: bold; font-size: 1.1rem;">
                            <fmt:formatNumber value="${linea.producto.precio * linea.cantidad}" type="currency" currencySymbol="€"/>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="factura-total-row">
            <span>Base Imponible:</span>
            <span><fmt:formatNumber value="${factura.importe}" type="currency" currencySymbol="€"/></span>
        </div>
        <div class="factura-total-row factura-total-border">
            <span>IVA (21%):</span>
            <span><fmt:formatNumber value="${factura.iva}" type="currency" currencySymbol="€"/></span>
        </div>
        <div class="factura-total-row factura-total-final">
            <span>TOTAL A PAGAR:</span>
            <span><fmt:formatNumber value="${factura.importe + factura.iva}" type="currency" currencySymbol="€"/></span>
        </div>

        <div class="factura-actions" style="margin-top: 40px; text-align: center;">
            <form action="${contexto}/FrontController" method="post" style="display:inline-block;">
                <input type="hidden" name="accion" value="inicio">
                <button type="submit" class="btn-primary" style="padding: 15px 30px; font-size: 1.1rem;">
                    Volver al Inicio
                </button>
            </form>
        </div>

    </div>
</div>

<jsp:include page="/INC/pie.jsp" />