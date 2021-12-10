<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <table style="width: 66%; border: 10px white solid; margin-left: 17%;">
            <tr style="background-color: white; ">
                <td style="border: transparent; padding-bottom: 10px;">
                    <b style="font-size: large;"><c:out value="${user.username}"/></b>
                </td>
                <td style="border: transparent; float: right; padding-bottom: 10px;">
                    <spring:url value="/users/{userId}/edit" var="userid">
                        <spring:param name="userId" value="${user.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(userid)}">edit user</a>
                </td>
            </tr>
            <tr>
                <td style="border: transparent; background-color: white; padding-bottom: 10px;">
                    <c:out value="${user.email}"/>
                </td>
                <td style="border: transparent; background-color: white; padding-bottom: 10px;"></td>
            </tr>
            
    </table>
    
    <table style="width: 66%; background-color: white; margin-left: 17%; margin-top: 3%;">
        <thead>
            <tr>
                <th style="width: 150px;">Partidas jugadas</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${user.players}" var="player">
                <tr>
                    <td>
                        <c:out value="${player.game.name}" />
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>


    <table style="width: 66%; background-color: white; margin-left: 17%; margin-top: 3%;">
        <thead>
            <tr>
                <th style="width: 150px;">Logros</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${user.achievements}" var="achievement">
                <tr>
                    <td>
                        <c:out value="${achievement.name}" />
                        </a>
                    </td>
                    <td>
                        <c:out value="${achievement.description}" />
                        </a>
                    </td>
                    <td>
                        <c:out value="${achievement.rewardPoints}" />
                        Puntos</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</petclinic:layout>