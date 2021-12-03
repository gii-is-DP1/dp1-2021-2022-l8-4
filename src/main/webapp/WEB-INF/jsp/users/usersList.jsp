<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <h2>Users</h2>
        <table id="usersTable" class="table table-striped">
            <thead>
                <tr>
                    <th style="width: 150px;">Username</th>
                    <th style="width: 200px;">Email</th>
                    <th style="width: 120px">Password</th>

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
                                    <c:out value="${user.password}"/>
                                </td>
                                <td>
                                    <spring:url value="/users/{userId}/edit" var="userid">
                                        <spring:param name="userId" value="${user.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(userid)}">edit user</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5">No users available</td>
                        </tr>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${size==1}">
                        <div class="panel">
                            ${size} user on page ${number+1}/${totalPages}
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
                            ${size} users on page ${number+1}/${totalPages}
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
</petclinic:layout>

