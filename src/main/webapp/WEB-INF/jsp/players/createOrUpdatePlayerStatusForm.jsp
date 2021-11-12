<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="players">
    <jsp:body>
        <h2><c:if test="${playerStatus['new']}">New </c:if>PlayerStatus</h2>

        <b>Player</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 150px;">Monster Name</th>
                <th style="width: 120px;">Life Points</th>
                <th style="width: 120px">Victory Points</th>
                <th style="width: 120px">Energy Points</th>
                <th style="width: 150px">Location</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${playerStatus.player.monsterName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="playerStatus" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:selectField label="Status" name="status"/>
                <petclinic:inputField label="Amount" name="amount"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="playerId" value="${playerStatus.player.id}"/>
                    <button class="btn btn-default" type="submit">Add Status</button>
                </div>
            </div>
        </form:form>

        <br/>
        <b>Previous Status</b>
        <table class="table table-striped">
            <tr>
                <th>Status</th>
                <th>Amount</th>
            </tr>
            <c:forEach var="playerStatus" items="${playerStatus.player.status}">
                <c:if test="${!playerStatus['new']}">
                    <tr>
                        <td><c:out value="${playerStatus.status}"/></td>
                        <td><c:out value="${playerStatus.amount}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>