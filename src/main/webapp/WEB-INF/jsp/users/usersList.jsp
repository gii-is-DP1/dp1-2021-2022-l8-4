<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

<kingoftokyo:layout pageName="users">
    <h2>Usuarios</h2>
        <table id="usersTable" class="table table-striped">
            <thead>
                <tr>
                    <th style="width: 150px;">Usuario</th>
                    <th style="width: 200px;">Email</th>
                    <th style="width: 140px">Ultimo usuario en acceder </th>
                    <th style="width: 150px">Ultimo acceso </th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${users.size() > 0}">
                        <c:forEach items="${users}" var="user">
                            <tr>
                                <td>
                                    <spring:url value="/users/profile/{userId}" var="userid">
                                        <spring:param name="userId" value="${user.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(userid)}">
                                    <c:out value="${user.username}" />
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${user.email}"/>
                                </td>
                                <td>
                                    <c:out value="${user.modifier}"/>
                                </td>
                                <td>
                                    <kingoftokyo:localDateTime dateTimeValue="${user.lastModifiedDate}"></kingoftokyo:localDateTime>
                                </td>
                                <td>
                                    <spring:url value="/users/{userId}/edit" var="userid">
                                        <spring:param name="userId" value="${user.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(userid)}">editar usuario</a>
                                    <br>
                                    <spring:url value="/users/delete/{userId}" var="userid">
                                        <spring:param name="userId" value="${user.id}"/>
                                    </spring:url>
                        
                                        <a href="${fn:escapeXml(userid)}">eliminar usuario</a>
                                   
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5">No hay usuarios disponibles</td>
                        </tr>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${size==1}">
                        <div class="panel">
                            ${size} usuario en la pagina ${number+1}/${totalPages}
                        </div>
                        <ul class="pagination">
                            <c:forEach begin="0" end="${totalPages-1}" var="page">
                                <li class="page-item">
                                    <a href="users?page=${page+1}" class="page-link">
                                        ${page+1}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <div class="panel">
                            ${size} usuarios en la pagina ${number+1}/${totalPages}
                        </div>
                        <ul class="pagination">
                            <c:forEach begin="0" end="${totalPages-1}" var="page">
                                <li class="page-item">
                                    <a href="users?page=${page+1}" class="page-link">
                                        ${page+1}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
</kingoftokyo:layout>

