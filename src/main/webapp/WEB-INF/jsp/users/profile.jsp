<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

<kingoftokyo:layout pageName="profile">
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
    
    <table style="border: 10px white solid; width: 66%; background-color: white; margin-left: 17%; margin-top: 3%;">
        <thead>
            <tr>
                <th style="width: 150px;">Partidas jugadas</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${user.players}" var="player">
                <c:if test="${player.game.isFinished()}">
                    <tr style="border: 1px #7800bd ;border-style: groove;border-radius: 5px; display: flex; margin-bottom: 2%;">
                        <td style="width: 50px;padding-top: 3px; padding-bottom: 3px;">
                            <c:choose>
                                <c:when test="${player.game.winner==user.username}">
                                    <img class="img-responsive" src="/resources/images/trofeo.png"/>
                                </c:when>    
                                <c:otherwise>
                                    <img class="img-responsive" src="/resources/images/game-over.png" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="padding-top: 18px; padding-bottom: 6px; padding-right: 12px;">
                            <c:out value="${player.game.name}" />
                            </a>
                        </td>
                        <td style="padding-top: 18px; padding-bottom: 6px;">
                            <c:out value="${player.game.endTime}" />
                            </a>
                        </td>
                    </tr>             
                </c:if>
            </c:forEach>
        </tbody>
    </table>


    <table style="border: 10px white solid; width: 66%; background-color: white; margin-left: 17%; margin-top: 3%;">
        <thead>
            <tr>
                <th style="width: 150px;">Logros</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${user.achievements}" var="achievement">
                <tr style="border: 1px #7800bd ;border-style: groove;border-radius: 5px; display: flex; margin-bottom: 2%;">
                    <td style="width: 40px; padding-top: 5px;">
                        <img class="img-responsive" src="/resources/images/medalla.png"/>    
                    </td>
                    <td style="padding-top: 15px; padding-bottom: 6px; padding-right: 12px; padding-left: 12px;">
                        <c:out value="${achievement.name}" />
                        </a>
                    </td>
                    <td style="padding-top: 15px; padding-bottom: 6px; padding-right: 12px;">
                        <c:out value="${achievement.description}" />
                        </a>
                    </td>
                    <td style="padding-top: 15px; padding-bottom: 6px;">
                        <c:out value="${achievement.rewardPoints}" />
                        Puntos</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</kingoftokyo:layout>