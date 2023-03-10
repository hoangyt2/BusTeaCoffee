<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../../admin/includes/hd.jsp"></jsp:include>
<jsp:include page="../../admin/includes/header.jsp"></jsp:include>
<jsp:include page="../../admin/includes/sidebar1.jsp"></jsp:include>
<jsp:include page="../../admin/includes/container.jsp"></jsp:include>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<div class="card mb-5 mb-xl-10" id="kt_profile_details_view">
    <!--begin::Card header-->
    <div class="card-header cursor-pointer">
        <!--begin::Card title-->
        <div class="card-title m-0">
            <h3 class="fw-bold m-0"></h3>
        </div>
        <!--end::Card title-->
        <!--begin::Action-->
<%--        <span class="btn btn-primary align-self-center save-data">Lưu thông tin</span>--%>
        <!--end::Action-->
    </div>
    <!--begin::Card header-->
    <!--begin::Card body-->
    <div class="card-body p-9">
        <!--begin::Wrapper-->


        <h3>Danh sách hoá đơn</h3>
        <div class="d-flex flex-stack ">

            <table class="table table-row-dashed table-row-gray-300 gy-7" id="tabel-invoice">
                <!--begin::Table head-->
                <thead>
                <!--begin::Table row-->
                <tr class="text-start text-muted fw-bold fs-7 text-uppercase gs-0">
                    <th scope="col" class="min-w-100px">Id Hoá Đơn</th>
                    <th scope="col" class="min-w-100px">Nhân viên</th>
                    <th scope="col" class="min-w-100px">Tên Bàn</th>
                    <th scope="col" class="min-w-100px">Tên khác hàng</th>
                    <th scope="col" class="min-w-100px">Tổng tiền</th>
                    <th scope="col" class="min-w-100px">Trạng thái</th>
                    <th scope="col" class="min-w-100px">Hành động</th>
                </tr>
                <!--end::Table row-->
                </thead>

                <tbody class="text-gray-600 fw-semibold">
                <c:forEach items="${listInvoice}" var="item">
                    <tr class="text-start">
                        <td>${item.id}</td>
                        <td>${item.employee.name}</td>
                        <td>${item.groupTable.groupName}</td>
                        <td>${item.customerName}</td>
                        <td><fmt:formatNumber pattern="#,###" value="${item.totalMoney}"/> ₫</td>
                        <td>
                            <c:if test="${item.status == 1}">
                                <span class="badge badge-danger">Chưa thanh toán</span>
                            </c:if>
                            <c:if test="${item.status == 2}">
                                <span class="badge badge-success">Đã thanh toán</span>
                            </c:if>
                        </td>
                        <td class="text-left">
                            <a href="/staff/invoice/detail/${item.id}" data-action="${item.id}"
                               class="btn btn-icon btn-primary  btn-sm btn-icon-md btn-circle"
                               data-toggle="tooltip" data-placement="top" title="Sửa">
                                <i class="fa fa-edit"></i>
                            </a>
                            <c:if test="${item.status != 2}">
                                <a href="#" data-action="${item.id}" data-total="${item.totalMoney}"
                                   class="btn btn-icon btn-success  btn-sm btn-icon-md btn-circle invoice-payment"
                                   data-toggle="tooltip" data-placement="top" title="Thanh Toán">
                                    <i class="fa fa-money-bill"></i>
                                </a>
                            </c:if>
                            <span data-action="${item.id}"
                                  class="btn btn-icon btn-danger delete-btn btn-sm btn-icon-md btn-circle"
                                  data-toggle="tooltip" data-placement="top" title="Xóa">
                                <i class="fa fa-trash"></i>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <ul class="pagination">
            <c:if test="${ allPage > 1}">
                <c:if test="${ currentPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/staff/invoice/${currentPage - 1}?limit=${limit}">
                            <i class="fa fa-angle-left"></i>
                        </a>
                    </li>
                </c:if>
                <c:if test="${ currentPage == 1}">
                    <li class="page-item disabled">
                        <a class="page-link" href="/staff/invoice/${currentPage - 1}?limit=${limit}">
                            <i class="fa fa-angle-left"></i>
                        </a>
                    </li>
                </c:if>
                <c:forEach var="i" begin="1" end="${allPage}">
                    <c:if test="${ i == currentPage}">
                        <li class="page-item active"><a href="/staff/invoice/${i}?limit=${limit}" class="page-link">${i}</a></li>
                    </c:if>
                    <c:if test="${ i != currentPage}">
                        <li class="page-item"><a href="/staff/invoice/${i}?limit=${limit}" class="page-link">${i}</a></li>
                    </c:if>
                </c:forEach>
                <c:if test="${ currentPage == allPage}">
                    <li class="page-item disabled">
                        <a class="page-link" href="/staff/invoice/${currentPage + 1}?limit=${limit}">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </li>
                </c:if>
                <c:if test="${ currentPage < allPage}">
                    <li class="page-item ">
                        <a class="page-link" href="/staff/invoice/${currentPage + 1}?limit=${limit}">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </li>
                </c:if>
            </c:if>
        </ul>
    </div>
</div>

</div>
<jsp:include page="../../admin/includes/footer.jsp"></jsp:include>
<jsp:include page="../../admin/includes/end.jsp"></jsp:include>
<script>

    $(document).on("click", ".delete-btn", function () {
        var id = $(this).attr("data-action");
        var url = "/staff/invoice/" + id;
        var that = $(this);
        swal.fire({
            title: 'Bạn có chắc chắn muốn xóa?',
            text: "Bạn sẽ không thể khôi phục lại dữ liệu này!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Vâng, xóa nó!',
            cancelButtonText: 'Không, hủy bỏ!'
        }).then((result) => {
            if (result.value) {
                $.ajax({
                    url: url,
                    type: "DELETE",
                    success: function (resutl) {
                        console.log(resutl);
                        if (resutl.check == true) {
                            toastr.success(resutl.value);
                            that.closest("tr").remove();
                        } else {
                            toastr.error(resutl.value);
                        }
                    }
                });
            }
        });
    });

    ///handel invoice-save
    $(document).on("click", ".invoice-payment", function () {
        var id = $(this).attr("data-action");
        var totalMoney = $(this).attr("data-total");
        let data = {
            idInvoice: id,
            toltalMoney: totalMoney
        }
        $.ajax({
            url: "/staff/invoice/payment",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (result) {
                if (result.check == true) {
                    toastr.success(result.value);
                    window.open(result.paymentUrl);
                }
            }
        })
    });

</script>