<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="userskot">
                            <h2>Users</h2>

                            <table id="usersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Username</th>
                                        <th style="width: 200px;">Email</th>
                                        <th style="width: 120px">Password</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${userskot}" var="user">
                                        <tr>
                                            <td>
                                                <c:out value="${user.username}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${user.email}" />
                                            </td>
                                            <td>
                                                <c:out value="${user.password}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </petclinic:layout>