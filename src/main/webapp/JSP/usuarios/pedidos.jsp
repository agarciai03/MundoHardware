<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Mis Pedidos" />
</jsp:include>

<div class="container-main container-center">
    <div class="form-card form-card-lg">
        <h2 class="form-title"><i class="fas fa-box-open"></i> Historial de Pedidos</h2>

        <c:choose>
            <c:when test="${empty misPedidos}">
                <p style="text-align:center; color:#aaa; padding:20px;">No has realizado ningún pedido todavía.</p>
                <div style="text-align: center; margin-top: 20px;">
                    <form action="${contexto}/FrontController" method="post">
                        <input type="hidden" name="accion" value="inicio">
                        <button type="submit" class="btn-primary">Ir a la Tienda</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="orders-table-container">
                    <table class="orders-table" style="width: 100%; border-collapse: collapse; text-align: left;">
                        <thead>
                            <tr style="border-bottom: 2px solid #444;">
                                <th style="padding: 10px;">ID Pedido</th>
                                <th style="padding: 10px;">Fecha</th>
                                <th style="padding: 10px;">Estado</th>
                                <th style="padding: 10px;">Importe Total</th>
                                <th style="padding: 10px;">Factura</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ped" items="${misPedidos}">
                                <tr style="border-bottom: 1px solid #333;">
                                    <td style="padding: 10px;">#${ped.idPedido}</td>
                                    <td style="padding: 10px;">${ped.fecha}</td>
                                    <td style="padding: 10px;">
                                        <span class="badge badge-success" style="background: #28a745; color: white; padding: 3px 8px; border-radius: 3px;">
                                            ${ped.estado == 'f' ? 'Finalizado' : 'En Carrito'}
                                        </span>
                                    </td>
                                    <td style="padding: 10px; font-weight:bold;"><fmt:formatNumber value="${ped.importe + ped.iva}" type="currency" currencySymbol="€"/></td>
                                    <td style="padding: 10px;">
                                        <form action="${contexto}/PedidoController" method="post" style="margin:0;">

                                            <input type="hidden" name="accion" value="verFactura">
                                            <input type="hidden" name="idPedido" value="${ped.idPedido}">
                                            <button type="submit" class="btn-primary" style="padding:5px 10px; font-size:0.8rem;">
                                                <i class="fas fa-file-invoice"></i> Ver
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/INC/pie.jsp" />