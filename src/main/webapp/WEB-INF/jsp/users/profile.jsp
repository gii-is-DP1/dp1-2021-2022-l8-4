<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="users">
    <table class="table table-striped">
            <tr>
            <td><b><c:out value="${user.username}"/></b></td>
            </tr>
            <tr>
                <td><c:out value="${user.email}"/></td>
                <td></td>
            </tr>
            <td>
                <spring:url value="/users/{userId}/edit" var="userid">
                    <spring:param name="userId" value="${user.id}"/>
                </spring:url>
                <a href="${fn:escapeXml(userid)}">edit user</a>
            </td>
    </table>
</petclinic:layout>