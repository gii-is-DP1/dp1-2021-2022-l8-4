<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="players">
                            <h2>Players</h2>

                            <table id="playersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Monster Name</th>
                                        <th style="width: 120px;">Life Points</th>
                                        <th style="width: 120px">Victory Points</th>
                                        <th style="width: 120px">Energy Points</th>
                                        <th style="width: 150px">Location</th>
                                    </tr>
                                </thead>
                                <tbody>
                                        <tr>
                                            <td>
                                                <c:out value="${player.monsterName}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.lifePoints}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.victoryPoints}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.energyPoints}" />
                                            </td>
                                            <td>
                                                <c:out value="${player.location}" />
                                            </td>
                                        </tr>
                                </tbody>
                            </table>
                            <table id="playerStatusTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Status</th>
                                        <th style="width: 150px;">Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${playerStatus}" var="pstatus">
                                        <tr>
                                            <td>
                                                <c:out value="${pstatus.status}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${pstatus.amount}" />
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </petclinic:layout>