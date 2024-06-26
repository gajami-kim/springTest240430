<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp" />
<div class="container-sm">
	<h1>Board List Page</h1> <br>
	
	<!-- 검색라인 -->
	<form action="/board/list" method="get">
		<div class="input-group mb-3">
		  <select class="form-select" name="type" id="inputGroupSelect02">
		    <option ${ph.pgvo.type eq null ? 'selected' : '' }>Choose...</option>
		    <option value="t" ${ph.pgvo.type eq 't' ?  'selected' : ''}>title</option>
		    <option value="w" ${ph.pgvo.type eq 'w' ?  'selected' : ''}>writer</option>
		    <option value="c" ${ph.pgvo.type eq 'c' ?  'selected' : ''}>content</option>
		    <option value="tc" ${ph.pgvo.type eq 'tc' ?  'selected' : ''}>title&content</option>
		    <option value="wc" ${ph.pgvo.type eq 'wc' ?  'selected' : ''}>writer&content</option>
		    <option value="tw" ${ph.pgvo.type eq 'twc' ?  'selected' : ''}>title&writer</option>
		    <option value="twc" ${ph.pgvo.type eq 'twc' ?  'selected' : ''}>all</option>
		  </select>
	  	<input type="text" name="keyword" class="form-control" aria-label="Text input with segmented dropdown button" value="${ph.pgvo.keyword }" placeholder="Search..">
		<input type="hidden" name="pageNo" value="1">
		<input type="hidden" name="qty" value="10">
		<button type="submit" class="btn btn-primary position-relative">
		  Search
		   <span class="badge text-bg-secondary text-bg-light">${ph.totalCount }</span>
		</button>
		</div>		
	</form>
	
	<table class="table table-striped-columns">
		<thead>
			<tr>
				<th>#</th>
				<th>Title</th>
				<th>Writer</th>
				<th>Reg Date</th>
				<th>Read Count</th>
			</tr>
		</thead>
		<c:forEach items="${list }" var="bvo">
		<tbody>
			<tr>
				<td>${bvo.bno }</td>
				<td><a href="/board/detail?bno=${bvo.bno }">${bvo.title } 
					<c:if test="${bvo.hasFile ne 0 }">[file:${bvo.hasFile }]</c:if></a>
					<c:if test="${bvo.cmtQty ne 0 }">[${bvo.cmtQty }]</c:if>
				</td>
				<td>${bvo.writer }</td>
				<td>${bvo.regDate }</td>
				<td>${bvo.readCount }</td>
			</tr>
		</tbody>
		</c:forEach>
	</table>
	
	<!-- 페이지네이션 라인 -->
	<nav aria-label="Page navigation example">
	  <ul class="pagination justify-content-center">
	  	
	  	<!-- 이전 -->
	  	
	    <li class="page-item ${ph.prev eq false? 'disabled' : '' }">
	      <a class="page-link" href="/board/list?pageNo=${ph.startPage-1 }&qty=${ph.pgvo.qty }&type=${ph.pgvo.type }&keyword=${ph.pgvo.keyword }" aria-label="Previous">
	        <span aria-hidden="true">&laquo;</span>
	      </a>
	    </li>
	  
	    <c:forEach begin="${ph.startPage }" end="${ph.endPage }" var="i">
		    <li class="page-item ${ph.pgvo.pageNo eq i? 'active' : '' }"><a class="page-link" href="/board/list?pageNo=${i }&qty=${ph.pgvo.qty }&type=${ph.pgvo.type }&keyword=${ph.pgvo.keyword }">${i }</a></li>	    
	    </c:forEach>
	    
	    <!-- 다음 -->
	    <li class="page-item ${ph.next eq false? 'disabled' : '' }">
	      <a class="page-link" href="/board/list?pageNo=${ph.endPage+1 }&qty=${ph.pgvo.qty }&type=${ph.pgvo.type }&keyword=${ph.pgvo.keyword }" aria-label="Next">
	        <span aria-hidden="true">&raquo;</span>
	      </a>
	    </li>
	  
	  </ul>
	</nav>

	<script type="text/javascript">
		const msg_bd_remove = `<c:out value="${msg_bd_remove}" />`;
		console.log(msg_bd_remove);
		if(msg_bd_remove=="1") {
			alert("삭제완료");
		}
	</script>
</div>    
<jsp:include page="../layout/footer.jsp" />