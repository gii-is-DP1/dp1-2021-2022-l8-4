<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

                        <kingoftokyo:layout pageName="players">
                            <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th style="width: 150px;">Game Name</th>
                                    <th style="width: 120px;">Creator</th>
                                    <th style="width: 120px">Turn</th>
                                    <th style="width: 120px">Winner</th>
                                    <th style="width: 150px">Start time</th>
                                    <th style="width: 150px">End time</th>
                                </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <c:out value="${game.name}" />
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${game.creator}" />
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${game.turn}" />
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${game.winner}" />
                                </td>
                                <td>
                                    <c:out value="${game.startTime}" />
                                </td>
                                <td>
                                    <c:out value="${game.endTime}" />
                                </td>
                            </tr>
                            </tbody>
                        </table>

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
                                    <c:forEach items="${players}" var="player">
                                        <tr>
                                            <td>
                                                <c:out value="${player.monster.getName()}" />
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
                                    </c:forEach>
                                </tbody>
                            </table>
                        </kingoftokyo:layout>