<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Personal Information <br/> <i> Thông Tin Cá Nhân</i> </h3>
			<div class="row register-form">
				<form action="${contextPath }/ekyc-enterprise/ekyc/step3" style="width: 100%;" method="post" id="submitForm" enctype='multipart/form-data'>
						<div class="tab">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group ">
									<label class="form-label" style="font-weight: bold;">ID Card No.<br/><i style="font-weight: normal;">Số CCCD hoặc CMND</i> </label>
									<input type="text" class="form-control" value="${ocr.soCmt }" readonly="readonly" name="soCmt"/>
								</div>
							</div>	
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Full Name <br/><i style="font-weight: normal;">Họ và Tên</i></label>
									<input type="text" class="form-control" value="${ocr.hoVaTen }"  name="hoVaTen"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Expiration Date<br/><i style="font-weight: normal;">Ngày Hết Hạn</i></label>
									<input type="text" class="form-control" value="${ocr.ngayHetHan }"  name="ngayHetHan"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Issue Date<br/><i style="font-weight: normal;">Ngày Cấp</i></label>
									<input type="text" class="form-control" value="${ocr.ngayCap }"  name="ngayCap"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Date of Birth<br/><i style="font-weight: normal;">Ngày Sinh</i></label>
									<input type="text" class="form-control" value="${ocr.namSinh }"  name="namSinh"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Issue Place<br/><i style="font-weight: normal;">Nơi Cấp</i></label>
									<input type="text" class="form-control" value="${ocr.noiCap }"  name="noiCap"/>
								</div>
							</div>
						</div>
						<hr/>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Nationality<br/><i style="font-weight: normal;">Quốc Tịch</i></label>
									<input type="text" class="form-control" value="${ocr.quocTich }"  name="quocTich"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Visa/ TR Card (for foreigner)<br/><i style="font-weight: normal;">Visa/ Thẻ TR (cho người ngoại quốc)</i></label>
									<input type="text" class="form-control" value="" id="visa" name="visa" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Tax Code<br/><i style="font-weight: normal;">Mã Số Thuế</i></label>
									<input type="text" class="form-control" value="" id="maSoThue"  name="maSoThue" />
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Residential Status<br/><i style="font-weight: normal;">Tình Trạng Cư Trú </i></label>
									<select class="form-control" name="tinhTrangCuTru">
										<option value="Resident">Resident</option>
										<option value="Non-resident">Non-resident</option>
									</select>
								</div>
							</div>
						</div>
						<hr/>
						<h4>Contact No.:<br/><i style="font-weight: normal;font-size: 20px;">Thông Tin Liên Hệ </i></h4>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Home<br/><i style="font-weight: normal;">Nhà</i></label>
									<input type="text" class="form-control" value="" id="diaChiNha" name="diaChiNha"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Mobile<br/><i style="font-weight: normal;">Số Điện Thoại</i></label>
									<input type="text" class="form-control" value="" id="mobile" name="mobile"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Office<br/><i style="font-weight: normal;">Văn Phòng</i></label>
									<input type="text" class="form-control" value="" id="vanPhong" name="vanPhong"/>
								</div>
							</div>
						</div>
						<hr/>
						<!-- <h4>Permanent Registered Address<br/><i style="font-weight: normal;font-size: 20px;">Hộ Khẩu Thường Trú </i></h4> -->
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
								<label class="form-label" style="font-weight: bold;">Permanent Registered Address<br/><i style="font-weight: normal;">Hộ Khẩu Thường Trú </i></label>
									<input type="text" class="form-control" value="${ocr.noiTru }" id="noiTru"  name="noiTru"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<!-- <label class="form-label" style="font-weight: bold;">Email</label> -->
									<label class="form-label" style="font-weight: bold;">Email<br/><i style="font-weight: normal;">Thư Điện Tử</i></label>
									<input type="text" class="form-control" value="" id="email2" name="email2"/>
								</div>
							</div>
						</div>
					</div>
					<button type="submit" class="btnRegister"  value="Saved/ Lưu thông tin" >Saved/ <i>Lưu thông tin</i></button>
					<button class="btnRegister" type="button"  style="background: #CCC;color: black;width: 150px;" onclick="javascript:location.href='${contextPath }/ekyc-enterprise/ekyc?<c:out value="${params.queryParams }"></c:out>'">Back/ <i>Quay lại</i></button>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("#submitForm").validate({
        rules: {
        	
        	code: {required: true}
        	
        }
    });
});



</script>

<%@include file="../layout/footer.jsp"%>
			
