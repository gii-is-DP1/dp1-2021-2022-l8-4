<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="users">

    <table class="table table-striped">
            <tr>
            <td style="border-top: transparent; padding-left: 5%;"><b><c:out value="${user.username}"/></b></td>
            <td style="border-top: transparent; float: right;">
                <spring:url value="/users/{userId}/edit" var="userid">
                <spring:param name="userId" value="${user.id}"/>
                </spring:url>
            </td>
            </tr>
            <tr style="background-color: #f9f9f9; border-top: transparent;">
                <td style="border-top: transparent; padding-left: 5%;"><c:out value="${user.email}"/></td>
                <td style="border-top: transparent;"></td>
            </tr>
    </table>
</petclinic:layout>