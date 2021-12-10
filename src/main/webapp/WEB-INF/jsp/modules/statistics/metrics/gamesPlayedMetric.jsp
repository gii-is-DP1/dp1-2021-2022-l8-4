<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <petclinic:layout pageName="gamesPlayedMetric">

                        <h2>P</h2>

                            <table id="metricTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Usuario</th>
                                        <th style="width: 120px;">Score</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${rows}" var="row">
                                        <tr>
                                            <td>
                                                <c:out value="${row.user.username}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${row.score}" />
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <a href="/achievements/new"><button type="button" >Nuevo logro</button></a>

                        </petclinic:layout>