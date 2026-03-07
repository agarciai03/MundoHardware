<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${param.titulo}</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilo.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <header>
            <div class="header-container">
                <div class="logo">
                    <form action="${contexto}/FrontController" method="post">
                        <input type="hidden" name="accion" value="inicio">
                        <button type="submit" class="btn-logo">
                            <div style="display:flex; align-items:center; gap:15px; color:white; font-size:2rem; font-weight:bold; text-transform:uppercase;">
                                <img src="${contexto}/imagen/logo/MundoHardware..png" alt="MundoHardware" title="MundoHardware" class="logo-img" onerror="this.style.display='none'">
                            </div>
                        </button>
                    </form>
                </div>

                <div class="search-bar">
                    <form action="${contexto}/FrontController" method="post" class="search-form">
                        <input type="hidden" name="accion" value="buscar">
                        <input type="text" name="query" class="search-input" placeholder="Buscar productos..." value="${param.query}">
                        <button type="submit" class="search-btn"><i class="fas fa-search"></i></button>
                    </form>
                </div>

                <div class="user-actions">

                    <form action="${contexto}/FrontController" method="post" class="form-link">
                        <input type="hidden" name="accion" value="inicio">
                        <button type="submit" class="btn-link" style="color:white; flex-direction:column;">
                            <i class="fas fa-home" style="font-size:1.5rem;"></i>
                            <span style="font-size:0.9rem;">Inicio</span>
                        </button>
                    </form>

                    <form action="${contexto}/PedidoController" method="post" class="form-link">
                        <input type="hidden" name="accion" value="verCarrito">
                        <button type="submit" class="btn-link" style="color:white; flex-direction:column;">
                            <div style="position:relative;">
                                <i class="fas fa-shopping-cart" style="font-size:1.5rem;"></i>
                                <span class="cart-counter">
                                    ${empty sessionScope.carrito ? 0 : sessionScope.carrito.lineas.size()}
                                </span>
                            </div>
                            <span style="font-size:0.9rem;">Carrito</span>
                        </button>
                    </form>

                    <c:choose>

                        <c:when test="${not empty sessionScope.usuario}">
                            <div class="user-dropdown" style="position:relative; display:inline-block;">
                                <button type="button" class="btn-link" style="color:white; flex-direction:column; align-items:center;" onclick="toggleDropdown()">
                                    <img src="${contexto}/imagen/avatar/${not empty sessionScope.usuario.avatar ? sessionScope.usuario.avatar : 'default.png'}" 
                                         title="Mi Perfil"
                                         style="width:40px; height:40px; border-radius:50%; object-fit:cover; border:2px solid white; margin-bottom:2px;"
                                         onerror="this.src='${contexto}/imagen/avatar/default.png'">
                                    <span style="font-size:0.8rem; font-weight:bold;">Mi Perfil ▼</span>
                                </button>

                                <div id="miDropdown" style="display:none; position:absolute; top:100%; right:0; background:#222; border:1px solid #444; border-radius:4px; min-width:160px; z-index:1000; box-shadow:0 4px 8px rgba(0,0,0,0.5);">
                                    <form action="${contexto}/UsuarioController" method="post" style="margin:0;">
                                        <input type="hidden" name="accion" value="verPerfil">
                                        <button type="submit" style="display:block; width:100%; padding:12px; text-align:left; background:none; border:none; color:white; cursor:pointer; border-bottom:1px solid #444;">👤 Mi Perfil</button>
                                    </form>
                                    <form action="${contexto}/PedidoController" method="post" style="margin:0;">

                                        <input type="hidden" name="accion" value="verPedidos">
                                        <button type="submit" style="display:block; width:100%; padding:12px; text-align:left; background:none; border:none; color:white; cursor:pointer; border-bottom:1px solid #444;">📦 Mis pedidos</button>
                                    </form>
                                    <form action="${contexto}/UsuarioController" method="post" style="margin:0;">

                                        <input type="hidden" name="accion" value="logout">
                                        <button type="submit" style="display:block; width:100%; padding:12px; text-align:left; background:none; border:none; color:#ff6b6b; cursor:pointer;">🚪 Salir</button>
                                    </form>
                                </div>
                            </div>

                            <script>
                                function toggleDropdown() {
                                    var drop = document.getElementById("miDropdown");
                                    drop.style.display = (drop.style.display === "none" || drop.style.display === "") ? "block" : "none";
                                }
                                // clicas fuera y se cierra
                                window.onclick = function (event) {
                                    if (!event.target.closest('.user-dropdown')) {
                                        var drop = document.getElementById("miDropdown");
                                        if (drop && drop.style.display === "block") {
                                            drop.style.display = "none";
                                        }
                                    }
                                }        
                            </script>
                        </c:when>

                        <c:otherwise>
                            <form action="${contexto}/UsuarioController" method="post" class="form-link">
                                <input type="hidden" name="accion" value="login">
                                <button type="submit" class="btn-link" style="color:white; flex-direction:column;">
                                    <i class="fas fa-user" style="font-size:1.5rem;"></i>
                                    <span style="font-size:0.9rem;">Acceder</span>
                                </button>
                            </form>
                        </c:otherwise>

                    </c:choose>

                </div>
            </div>
        </header>