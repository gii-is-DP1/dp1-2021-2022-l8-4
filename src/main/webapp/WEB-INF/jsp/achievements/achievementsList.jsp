<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="achievements">
    <h2>Achievements</h2>

    <table id="achievementsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Title</th>
            <th style="width: 200px;">Reward Points</th>
            <th style="width: 120px">Condition</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${achievements}" var="achievement">
            <tr>
                <td>
                    <c:out value="${achievement.name}"/></a>
                </td>
                <td>
                    <c:out value="${achievement.rewardPoints}"/>
                </td>
                <td>
                    <c:out value="${achievement.condition}"/>
                </td>
                <td>
                    <spring:url value="/achievements/delete/{achievementId}" var="achievementUrl">
                        <spring:param name="achievementId" value="${achievement.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(achievementUrl)}">Delete achievement</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>