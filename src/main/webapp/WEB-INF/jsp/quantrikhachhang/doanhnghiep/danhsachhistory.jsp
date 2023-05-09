<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="../../layout/js.jsp"%>
<style type="text/css">
.help-block {
	color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>
			Danh sách doanh nghiệp
		</h2>
	</div>
	<div class="box-body table-responsive no-padding">
		<table class="table table-striped table-hover table-bordered">
			<thead>
				<tr>
					<th style="width: 50px; text-align: center;">#</th>
					<th><spring:message code="ekycdn.ma_so_doanh_nghiep" /></th>
					<th><spring:message code="ekycdn.ten_doanh_nghiep" /></th>
					<th><spring:message code="ekycdn.ten_nguoi_quan_ly" /></th>
					<th><spring:message code="ekycdn.ten_nguoi_lien_he" /></th>
					<th><spring:message code="ekycdn.so_dien_thoai_nguoi_lien_he" /></th>
					<th><spring:message code="ekycdn.email_nguoi_lien_he" /></th>
					<th><spring:message code="ngay_tao" /></th>
					<th><spring:message code="trang_thai" /></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ekycDnHistorys}" var="item" varStatus="status">
					<tr>
						<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
						<%-- <td style="text-align: center;width: 50px;">
										<span >${status.index+1 }</span>
									</td> --%>
						<td><span><c:out value="${item.maDoanhNghiep }" /></span></td>
						<td><span><c:out value="${item.tenDoanhNghiep }" /></span></td>
						<td><span><c:out value="${item.tenNguoiQuanLy }" /></span></td>
						<td><span><c:out value="${item.tenNguoiLienHe }" /></span></td>
						<td><span><c:out
									value="${item.soDienThoaiNguoiLienHe }" /></span></td>
						<td><span><c:out value="${item.emailNguoiLienHe }" /></span>
						</td>
						<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss"
								value="${item.ngayTao }" /></td>
						<td><c:if test="${item.status eq 'success' }">
								<span style="color: blue;"><spring:message code="da_ky" /></span>
							</c:if>
							 <c:if test="${item.status ne 'success' }">
								<span style="color: red;"><spring:message code="chua_ky" /></span>
							</c:if>
						</td>
                         <td class="text-center">
										 <c:if test="${item.status eq 'success' }">
											<c:if test="${download eq true }">
												<a href="${contextPath}/download-ekyc-doanh-nghiep-history?id=${item.id}">
													<i class="fa fa-download"></i>
												</a>
											</c:if>
										</c:if> 
										 <c:if test="${xemchitiet eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/xem-ekyc-doanh-nghiep-history?id=${item.id}')" data-toggle="modal" target="_blank" class="text-info" style="margin-left: 5px;">
												<i class="fa fa-eye"></i>
											</a>
										</c:if>
										<%-- <c:if test="${xem eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-ekyc-doanh-nghiep?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
												<i class="fa fa-list-alt"></i>
											</a>
										</c:if>
										<c:if test="${xoa eq true }">
											<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/xoa-ekyc-doanh-nghiep?id=${item.id}')" class="text-danger" style="margin-left: 5px;">
												<i class="fa fa-trash"></i>
											</a>
										</c:if> --%>
									</td> 
					</tr>
				</c:forEach>
			</tbody>
		</table>

	<%@include file="../../layout/paginate.jsp"%> 
	</div>
</div>

<%@include file="../../layout/footerAjax.jsp"%>
