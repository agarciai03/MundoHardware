<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="${not empty producto ? producto.nombre : 'Detalle del Producto'}" />
    <jsp:param name="estilo" value="${contexto}/CSS/estilo.css" />
</jsp:include>

<jsp:include page="/INC/nav.jsp" />

<div class="detalle-wrapper">

    <c:choose>
        <c:when test="${not empty producto}">

            <article class="product-card-detail">
                <div class="detail-image-col">
                    <img src="${contexto}/imagen/${producto.imagen}" 
                         alt="${producto.nombre}"
                         onerror="this.src='${contexto}/imagen/productos/default.jpg'">
                </div>

                <div class="detail-info-col">
                    <div class="product-brand">${producto.marca}</div>
                    <h1 class="product-title">${producto.nombre}</h1>

                    <div class="product-price">
                        <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                    </div>

                    <div class="product-desc">
                        ${producto.descripcion}
                    </div>

                    <form action="${contexto}/PedidoController" method="POST">

                        <input type="hidden" name="accion" value="anadirCarrito">
                        <input type="hidden" name="idProducto" value="${producto.idProducto}">

                        <div class="purchase-controls">
                            <label for="cantidad">Cant:</label>
                            <input type="number" id="cantidad" name="cantidad" value="1" min="1" max="10" class="qty-input">

                            <button type="submit" class="btn-add-cart">
                                <span>Añadir al Carrito</span>
                            </button>
                        </div>
                    </form>

                    <form action="${contexto}/FrontController" method="POST" style="display:inline-block; margin-top: 20px;">
                        <input type="hidden" name="accion" value="inicio">
                        <button type="submit" class="back-link" style="background:none; border:none; padding:0; font:inherit; cursor:pointer; text-decoration:underline;">
                            &larr; Volver al Catálogo
                        </button>
                    </form>
                </div>
            </article>

        </c:when>

        <c:otherwise>
            <div class="error-box">
                <h2>Producto no encontrado</h2>
                <p>Lo sentimos, el producto que buscas no existe o ha sido eliminado.</p>
                <br>
                <form action="${contexto}/FrontController" method="POST" style="display:inline-block; width:auto;">
                    <input type="hidden" name="accion" value="inicio">
                    <button type="submit" class="btn-add-cart" style="width:100%;">
                        Volver a la Tienda
                    </button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<jsp:include page="/INC/pie.jsp" />