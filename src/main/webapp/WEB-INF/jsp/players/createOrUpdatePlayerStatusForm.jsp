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
                <td><c:out value="${player.monsterName}"/></td>
                <td><c:out value="${player.lifePoints}" /></td>
                <td><c:out value="${player.victoryPoints}"/></td>
                <td><c:out value="${player.energyPoints}"/></td>
                <td><c:out value="${player.location}"/></td>

            </tr>
        </table>

        <form:form modelAttribute="playerStatus" class="form-horizontal">
            <div class="form-group has-feedback">
                <div class="control-group">
                    <petclinic:selectField name="status" label="Status" names="${statusTypes}" size="2"/>
                </div>
                <petclinic:inputField label="Amount" name="amount"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="playerId" value="${playerStatus.player.id}"/>
                    <button class="btn btn-default" type="submit">Add Status</button>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>