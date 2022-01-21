<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <kingoftokyo:layout pageName="achievementsList">

                        <h2>
                            Logros
                            <br> 
                            <a href="/achievements/new"><button type="button" >Nuevo logro</button></a>
                         </h2>

                        
                            <table id="achievementsTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Nombre</th>
                                        <th style="width: 120px;">Descripcion</th>
                                        <th style="width: 120px;">Recompensa</th>
                                        <th style="width: 150px;">Metrica</th>
                                        <th style="width: 120px;">Meta</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${achievements}" var="achievement">
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
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${achievement.metric.getName()}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${achievement.goal}" />
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            

                        </kingoftokyo:layout>