<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <kingoftokyo:layout pageName="ranking">

                        <form class="form-horizontal" >
                            <div class="form-group has-feedback">
                                <select name="metric">
                                    <c:forEach items="${metrics}" var="m">
                                        <option value="${m}">${m.getName()}</option>
                                    </c:forEach>
                                </select>   
                            </div>

                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button class="btn btn-default" type="submit">Buscar por metrica</button>
                                </div>
                            </div>
                        </form>

                        <h2>${actualMetric.getName()}</h2>

                            <table id="metricTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Usuario</th>
                                        <th style="width: 120px;">Score</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:choose>
                                        <c:when test="${rows.size() > 0}">
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
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5">No ranking available</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:choose>
                                        <c:when test="${size==1}">
                                            <div class="panel">
                                                ${size} user on page ${number+1}/${totalPages}
                                            </div>
                                            <ul class="pagination">
                                                <c:forEach begin="0" end="${totalPages-1}" var="page">
                                                    <li class="page-item">
                                                        <a href="ranking?metric=${actualMetric}&page=${page+1}" class="page-link">
                                                            ${page+1}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="panel">
                                                ${size} users on page ${number+1}/${totalPages}
                                            </div>
                                            <ul class="pagination">
                                                <c:forEach begin="0" end="${totalPages-1}" var="page">
                                                    <li class="page-item">
                                                        <a href="ranking?metric=${actualMetric}&page=${page+1}" class="page-link">
                                                            ${page+1}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>

                        </kingoftokyo:layout>