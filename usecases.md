Phần mềm bao gồm 3 loại người dùng tương tác: người dùng không có tài khoản (guest), người dùng có tài khoản (customer), người quản trị hệ thống (admin).
Người dùng không có tài khoản (guest) có các chức năng:
-      Xem danh sách sản phẩm
-      Xem chi tiết của từng sản phẩm từ danh sách sản phẩm.
-      Chọn mua từng sản phẩm (có thể chọn mua từ danh sách sản phẩm hay từ màn hình chi tiết của từng sản phẩm), sản phẩm sau khi chọn mua sẽ được đưa vào trong giỏ hàng.
-      Xem giỏ hàng (danh sách sản phẩm đã chọn mua)
-      Khi xem giỏ hàng, có thể chỉnh sửa số lượng của từng sản phẩm trong giỏ hàng (nếu chỉnh sửa số lượng là 0 thì bỏ sản phẩm đó ra khỏi giỏ hàng)
-      Có thể đăng ký tài khoản với các thông tin cần thiết (email không trùng với tài khoản khác).
Người dùng có tài khoản (customer) có thể thực hiện các chức năng của Người dùng không có tài khoản (guest), ngoài ra người dùng có tài khoản (customer) còn có thể:
-      Xử lý thanh toán (chức năng này thực hiện khi giỏ hàng đã có sản phẩm và người dùng đăng nhập thành công vào hệ thống):
Người quản trị hệ thống (admin) có thể thực hiện được chức năng như một người dùng có tài khoản (customer). Ngoài ra, chức năng khác dành cho người quản trị hệ thống (admin):
-      Tìm kiếm thông tin về sản phẩm/loại sản phẩm, tài khoản người dùng, các đơn đặt sản phẩm.
-      Quản lý thông tin sản phẩm/loại sản phẩm
-      Quản lý thông tin tài khoản người dùng
-      Quản lý thông tin đơn hàng trực tuyến
Đề tiểu luận:
Website giới thiệu, bán xe máy và phụ kiện trực tuyến.




1. Use Case: Xem chi tiết sản phẩm
Business Rules:

Sản phẩm phải tồn tại trong hệ thống
Hiển thị đầy đủ thông tin: tên, giá, mô tả, hình ảnh, số lượng tồn kho
Không yêu cầu đăng nhập (guest có thể xem)
Phải hiển thị thông tin loại sản phẩm (category)
2. Use Case: Đăng nhập
Business Rules:

Email phải tồn tại trong hệ thống
Mật khẩu phải khớp với mật khẩu đã mã hóa trong database
Sau khi đăng nhập thành công, tạo session/token cho user
Phân biệt role: customer vs admin
Giỏ hàng của guest (nếu có) phải được merge với giỏ hàng của user sau khi đăng nhập
3. Use Case: Đăng ký tài khoản
Business Rules:

Email phải unique (không trùng với tài khoản khác)
Email phải đúng định dạng
Mật khẩu phải đáp ứng yêu cầu bảo mật (độ dài tối thiểu)
Mật khẩu phải được mã hóa trước khi lưu vào database
Mặc định role là "customer"
Các thông tin bắt buộc: email, password, tên, số điện thoại, địa chỉ
4. Use Case: Thêm vào giỏ hàng
Business Rules:

Sản phẩm phải tồn tại và còn hàng
Số lượng thêm vào phải > 0
Số lượng thêm vào không được vượt quá số lượng tồn kho
Nếu sản phẩm đã có trong giỏ, cộng dồn số lượng (không tạo item mới)
Guest có thể thêm vào giỏ (lưu trong session/cookie)
Customer đã login lưu giỏ hàng vào database
5. Use Case: Thanh toán
Business Rules:

Bắt buộc phải đăng nhập (chỉ customer/admin mới được thanh toán)
Giỏ hàng phải có ít nhất 1 sản phẩm
Kiểm tra lại số lượng tồn kho của từng sản phẩm trước khi thanh toán
Tính tổng tiền đơn hàng (quantity × price cho mỗi item)
Tạo Order với trạng thái ban đầu (ví dụ: "PENDING")
Tạo các OrderItem tương ứng với các CartItem
Trừ số lượng tồn kho của sản phẩm
Xóa giỏ hàng sau khi thanh toán thành công
Lưu thông tin người đặt hàng (customer info)
6. Use Case: Xem giỏ hàng
Business Rules:

Guest: hiển thị giỏ hàng từ session/cookie
Customer đã login: hiển thị giỏ hàng từ database
Hiển thị thông tin mỗi item: tên sản phẩm, giá, số lượng, tạm tính (price × quantity)
Tính tổng tiền toàn bộ giỏ hàng
Kiểm tra và cảnh báo nếu sản phẩm trong giỏ không còn đủ số lượng tồn kho
7. Use Case: Chỉnh số lượng sản phẩm trong giỏ hàng
Business Rules:

Số lượng mới phải ≥ 0
Nếu số lượng = 0, xóa sản phẩm khỏi giỏ hàng
Nếu số lượng > 0, kiểm tra không vượt quá tồn kho
Cập nhật số lượng và tự động tính lại tổng tiền
Guest: cập nhật trong session/cookie
Customer: cập nhật trong database
Không cho phép số lượng âm

usecase chung (abstract): Một số Business Rules chung cho toàn hệ thống:

Giá sản phẩm phải > 0
Số lượng tồn kho không được âm
Mỗi sản phẩm phải thuộc về một loại sản phẩm (category)
Admin có thể thực hiện mọi chức năng của customer
Session/token có thời gian hết hạn
