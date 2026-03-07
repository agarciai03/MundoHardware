<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Catálogo MundoHardware" />
</jsp:include>

<div class="container-main">

    <aside>
        <div class="filter-box">
            <h3>Categorías</h3>
            <form action="${contexto}/FrontController" method="post">
                <input type="hidden" name="accion" value="buscar">

                <div style="max-height: 150px; overflow-y: auto; margin-bottom: 10px; padding-left: 5px;">
                    <c:forEach var="cat" items="${applicationScope.appCategorias}">
                        <label style="display:block; margin-bottom: 5px; cursor: pointer;">
                            <input type="checkbox" name="categoria" value="${cat.idCategoria}">
                            ${cat.nombre}
                        </label>
                    </c:forEach>
                </div>

                <button type="submit" class="btn-primary btn-block">Filtrar Categorías</button>
            </form>
        </div>

        <div class="filter-box">
            <h3>Marcas</h3>
            <form action="${contexto}/FrontController" method="post">
                <input type="hidden" name="accion" value="buscar">

                <div style="max-height: 150px; overflow-y: auto; margin-bottom: 10px; padding-left: 5px;">
                    <c:forEach var="m" items="${applicationScope.appMarcas}">
                        <label style="display:block; margin-bottom: 5px; cursor: pointer;">
                            <input type="checkbox" name="marca" value="${m}">
                            ${m}
                        </label>
                    </c:forEach>
                </div>

                <button type="submit" class="btn-primary btn-block">Filtrar Marcas</button>
            </form>
        </div>

        <div class="filter-box">
            <h3>Rango de Precio</h3>
            <form action="${contexto}/FrontController" method="post">
                <input type="hidden" name="accion" value="filtrarPrecio">

                <div style="display:flex; justify-content:space-between; margin-bottom: 10px; font-weight:bold;">
                    <span id="textoMin">${not empty minPrecioSel ? minPrecioSel : 0} €</span>
                    <span id="textoMax">${not empty maxPrecioSel ? maxPrecioSel : 1880} €</span>
                </div>

                <div style="position: relative; height: 30px;">
                    <div style="position: absolute; top: 10px; left: 0; right: 0; height: 6px; background: #ddd; border-radius: 5px;"></div>

                    <div id="barraColor" style="position: absolute; top: 10px; height: 6px; background: #007bff; border-radius: 5px;"></div>

                    <input type="range" id="inputMin" name="minPrecio" class="rango-doble"
                           min="0" max="1880" step="10" value="${not empty minPrecioSel ? minPrecioSel : 0}">

                    <input type="range" id="inputMax" name="maxPrecio" class="rango-doble"
                           min="0" max="1880" step="10" value="${not empty maxPrecioSel ? maxPrecioSel : 1880}">
                </div>

                <button type="submit" class="btn-secondary btn-block" style="margin-top:10px;">Aplicar Filtro</button>
            </form>
        </div>

        <div style="margin-top:20px;">
            <form action="${contexto}/FrontController" method="post">
                <input type="hidden" name="accion" value="inicio">
                <button type="submit" class="btn-block" style="background:transparent; border:1px solid #555; color:white; padding:10px; cursor:pointer;">
                    Restablecer filtros
                </button>
            </form>
        </div>
    </aside>

    <main>
        <c:if test="${not empty mensaje}">
            <div class="alert-message">${mensaje}</div>
        </c:if>

        <div class="product-grid">
            <c:forEach var="p" items="${listaProductos}">
                <article class="card">

                    <a href="javascript:void(0);" 
                       class="card-link"
                       onclick="abrirModal(this)"
                       data-id="${p.idProducto}"
                       data-nombre="${p.nombre}"
                       data-marca="${p.marca}"
                       data-precio="${p.precio}" <%-- Precio crudo para JS --%>
                       data-preciofmt="<fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="€"/>" <%-- Precio bonito --%>
                       data-descripcion="${p.descripcion}"
                       data-imagen="${contexto}/imagen/productos/${p.imagen}.jpg">

                        <img src="${contexto}/imagen/productos/${p.imagen}.jpg" 
                             alt="${p.nombre}"
                             title="${p.nombre}"
                             class="card-img-product"
                             onerror="this.src='${contexto}/imagen/productos/default.jpg'; this.onerror=null;">
                    </a>

                    <div>
                        <h4>${p.nombre}</h4>
                        <div class="price">
                            <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="€"/>
                        </div>

                        <form action="${contexto}/PedidoController" method="post">
                            <input type="hidden" name="accion" value="anadirCarrito">
                            <input type="hidden" name="idProducto" value="${p.idProducto}">
                            <button type="submit" class="btn-primary btn-block">Añadir al Carrito</button>
                        </form>
                    </div>
                </article>
            </c:forEach>
        </div>
    </main>
</div>

<div id="productoModal" class="modal-backdrop" onclick="cerrarModal(event)">
    <div class="modal-content">
        <button class="close-modal" onclick="cerrarModal(null)">&times;</button>

        <div class="modal-body">
            <div class="modal-img-col">
                <img id="modalImg" src="" title="Producto" alt="Producto" onerror="this.src='${contexto}/imagen/productos/default.jpg'">
            </div>

            <div class="modal-info-col">
                <h2 id="modalTitulo" style="margin-top:0; margin-bottom:10px; color:var(--text-main);">Nombre del Producto</h2>

                <div id="modalMarca" style="color:var(--header-footer-bg); font-weight:bold; margin-bottom:20px; text-transform:uppercase; letter-spacing:1px;">
                    MARCA
                </div>

                <div style="flex-grow:1; overflow-y:auto; margin-bottom:20px; color:#ccc; line-height:1.6; max-height:200px; padding-right:10px;">
                    <p id="modalDescripcion">Descripción detallada...</p>
                </div>

                <div style="border-top:1px solid #444; padding-top:20px; display:flex; justify-content:space-between; align-items:center;">

                    <div id="modalPrecio" class="price" style="font-size:2rem; margin:0;">0,00 €</div>

                    <form action="${contexto}/PedidoController" method="post" style="min-width: 150px;">
                        <input type="hidden" name="accion" value="anadirCarrito">
                        <input type="hidden" id="modalInputId" name="idProducto" value="">

                        <button type="submit" class="btn-primary" style="padding:15px 25px; font-size:1.1rem; width:100%;">
                            <i class="fas fa-cart-plus"></i> COMPRAR
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${contexto}/JS/catalogo.js"></script>

<jsp:include page="/INC/pie.jsp" />