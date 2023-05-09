<%@ page contentType="text/html; charset=UTF-8"%>
<div class="stepwizard" style="margin-bottom: 20px;">

 <div class="stepwizard-row setup-panel">
	<div class="stepwizard-step">
		<a href="#step-0" data-toggle="tooltip" data-placement="top" title="Guidelines
Hướng Dẫn" type="button" <c:choose><c:when test="${step eq '1'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 1}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>0</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-1" title="Upload Documents
Tải Tài Liệu"  type="button" <c:choose><c:when test="${step eq '2'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 2}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>1</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-2" title="Company Information
Thông Tin Công Ty" type="button" <c:choose><c:when test="${step eq '3'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 3}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>2</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-3" title="Legal Representative Information
Thông Tin của Đại Diện Pháp Luật" type="button" <c:choose><c:when test="${step eq '4'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 4}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>3</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-4" type="button" title="Chief Accountant or PIC of Accounting Information
Thông Tin của Kế Toán Trưởng hoặc Người Phụ Trách Kế Toán" <c:choose><c:when test="${step eq '5'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 5}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>4</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-44" title="Member’s Council, Board of Directors, Board of Management Information
Thông Tin Hội Đồng Thành Viên, Hội Đồng Quản Trị, Ban Điều Hành" type="button" <c:choose><c:when test="${step eq '6'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 6}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>5</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-5" title="Authorised Person(s) of Account Holder’s Representative Information
Thông Tin Người Được Đại Diện Chủ Tài Khoản Ủy Quyền Ký" type="button" <c:choose><c:when test="${step eq '7'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 7}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>6</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-6" title="Authorised Person(s) of Chief Accountant (Person In Charge of Accounting) Information
Thông Tin Người Được Kế Toán Trưởng (Người Phụ Trách Bộ Phận Kế Toán) Ủy Quyền Ký" type="button" <c:choose><c:when test="${step eq '8'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 8}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>7</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-14" title="Straight2Bank User Designation & Authorisation
Chỉ Định và Ủy Quyền Người Dùng Straight2Bank" type="button" <c:choose><c:when test="${step eq '9'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 9}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>8</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-10" type="button" title="Special Instruction
Chỉ Dẫn Đặc Biệt" <c:choose><c:when test="${step eq '10'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 10}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>9</a>
	</div>
	<div class="stepwizard-step">
		<a href="#step-11" type="button" title="Complete the Registration Process
Hoàn Tất Quá Trình Đăng Ký" <c:choose><c:when test="${step eq '11'}">class="btn btn-success btn-circle"</c:when><c:when test="${step > 11}">class="btn btn-default btn-circle"</c:when><c:otherwise>class="btn btn-default btn-circle disabled" disabled="disabled"</c:otherwise></c:choose>>10</a>
	</div>
</div> 
	
 	<!--  <div class="stepwizard-row setup-panel">
		<div class="stepwizard-step">
			<a href="#step-0" type="button" class="btn btn-success btn-circle">1</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-1" type="button" class="btn btn-default btn-circle">2</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-2" type="button" class="btn btn-default btn-circle " >3</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-3" type="button" class="btn btn-default btn-circle ">4</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-4" type="button" class="btn btn-default btn-circle ">5</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-44" type="button" class="btn btn-default btn-circle " >6</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-5" type="button" class="btn btn-default btn-circle " >7</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-6" type="button" class="btn btn-default btn-circle " >8</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-14" type="button" class="btn btn-default btn-circle " >9</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-10" type="button" class="btn btn-default btn-circle " >9</a>
		</div>
		<div class="stepwizard-step">
			<a href="#step-11" type="button" class="btn btn-default btn-circle " >10</a>
		</div>
	</div>  -->  
</div>
