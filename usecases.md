1. Use Case: Xem chi tiết sản phẩm
Actor: Guest - Người dùng chưa có tài khoản, Customer - Người dùng có tài khoản, Admin - Người quản trị hệ thống

Điều kiện bắt đầu: Người dùng (Guest, Customer hoặc Admin) đang ở trang danh sách sản phẩm và chọn một sản phẩm cụ thể để xem chi tiết.

Flow of events:

User/Customer/Admin nhấp vào 1 sản phẩm (xe máy hoặc phụ kiện) từ danh sách sản phẩm.

Phần mềm nhận yêu cầu truy cập chi tiết sản phẩm.

Phần mềm truy xuất thông tin chi tiết của sản phẩm (tên, mô tả, hình ảnh, giá, thông số kỹ thuật,....).

Hệ thống trả về thông tin chi tiết sản phẩm đã chọn.

Điều kiện kết thúc: Hệ thống hiển thị đầy đủ thông tin chi tiết của sản phẩm đã chọn (tên, mô tả, giá, hình ảnh, thông số kỹ thuật, v.v.) cho người dùng.

2. Use Case: Đăng nhập
Actor: Customer - Người dùng có tài khoản (đã đăng xuất), Admin - Người quản trị hệ thống (đã đăng xuất)

Điều kiện bắt đầu: Người dùng (Customer hoặc Admin) chưa đăng nhập vào hệ thống và đang ở giao diện chính hoặc trang yêu cầu xác thực.

Flow of events:

Customer/Admin chọn chức năng "Đăng nhập".

Phần mềm trả về trang đăng nhập (gồm ô nhập email, mật khẩu).

Customer/Admin điền email và mật khẩu vào biểu mẫu đăng nhập, sau đó bấm nút "Đăng nhập".

Phần mềm nhận được thông tin đăng nhập, xác thực email và mật khẩu với cơ sở dữ liệu. + Nếu không hợp lệ, trả về thông báo lỗi. + Nếu hợp lệ thì tạo phiên đăng nhập và xác định vai trò.

Phần mềm trả về trạng thái đăng nhập thành công.

Điều kiện kết thúc: Hệ thống xác thực thành công thông tin đăng nhập, tạo phiên làm việc cho người dùng tương ứng với vai trò (Customer hoặc Admin), và hiển thị trang chủ hoặc trang được yêu cầu. Nếu sai thông tin, hiển thị thông báo lỗi.

3. Use Case: Đăng ký tài khoản
Actor: Guest - Người dùng chưa có tài khoản

Điều kiện bắt đầu: Người dùng (Guest) chưa có tài khoản, đang ở giao diện chính và chọn chức năng "Đăng ký". Hệ thống sẵn sàng hiển thị biểu mẫu đăng ký.

Flow of events:

Guest chọn chức năng "Đăng ký".

Phần mềm trả về trang đăng ký (gồm ô nhập email, tên đăng nhập, mật khẩu và số điện thoại).

Guest điền thông tin vào biểu mẫu đăng ký sau đó bấm nút "Đăng ký".

Phần mềm nhận thông tin, kiểm tra tính hợp lệ của email (không trùng với tài khoản khác), nếu trùng, trả về thông báo lỗi.

Phần mềm lưu thông tin tài khoản mới vào cơ sở dữ liệu.

Phần mềm trả về thông báo đăng ký thành công.

Điều kiện kết thúc: Hệ thống lưu thành công thông tin tài khoản mới vào cơ sở dữ liệu, hiển thị thông báo đăng ký thành công và cho phép người dùng đăng nhập. Nếu thông tin không hợp lệ hoặc bị trùng (email, tên đăng nhập), hệ thống hiển thị thông báo lỗi và không tạo tài khoản.

4. Use Case: Thêm vào giỏ hàng
Actor: Guest - Người dùng chưa có tài khoản, Customer - Người dùng có tài khoản, Admin - Người quản trị hệ thống

Điều kiện bắt đầu: Người dùng (Guest/Customer/Admin) đang ở trang danh sách hoặc chi tiết sản phẩm và bấm "Thêm vào giỏ hàng" cho một sản phẩm hợp lệ (còn bán).

Flow of events:

User/Customer/Admin nhấn nút "Thêm vào giỏ hàng" tại một sản phẩm (từ trang danh sách hoặc trang chi tiết).

Phần mềm nhận yêu cầu, nếu là guest thì đưa đến trang đăng nhập, nếu là customer/admin thì thêm vào giỏ hàng liên kết với tài khoản.

Phần mềm trả về thông báo đã thêm sản phẩm thành công.

Điều kiện kết thúc: Sản phẩm được thêm vào giỏ hàng gắn với phiên/tài khoản (nếu là Guest thì sau khi đăng nhập), và hệ thống hiển thị thông báo thêm thành công.

5. Use Case: Thanh toán
Actor: Customer - Người dùng có tài khoản

Điều kiện bắt đầu: Customer đã đăng nhập; giỏ hàng có ít nhất 1 sản phẩm hợp lệ (còn hàng); hệ thống sẵn sàng kết nối phương thức thanh toán (COD/online).

Flow of events:

Customer nhấn vào nút "Thanh toán".

Phần mềm kiểm tra điều kiện: nếu không có sản phẩm -> trả về thông báo lỗi; nếu có sản phẩm, trả về biểu mẫu thông tin giao hàng và phương thức thanh toán.

Customer điền thông tin giao hàng và chọn phương thức thanh toán, sau đó nhấn nút "Xác nhận đơn hàng".

Phần mềm xử lý thanh toán, nếu thành công: tạo 1 đơn hàng trong cơ sở dữ liệu (gồm thông tin khách hàng, sản phẩm, địa chỉ...) thông qua usecase thêm đơn hàng.

Phần mềm xóa các sản phẩm trong giỏ hàng của Customer.

Phần mềm trả về thông báo đặt hàng thành công (COD/online).

Điều kiện kết thúc: Đơn hàng được tạo và lưu (thông tin giao hàng, phương thức thanh toán, chi tiết sản phẩm, tổng tiền); thanh toán được xác nhận/ghi nhận tồn kho được trừ/giữ; giỏ hàng được làm trống; hệ thống hiển thị (và/hoặc gửi email) xác nhận đặt hàng. Nếu thanh toán thất bại hoặc hết hàng, hiển thị lỗi và không tạo đơn.

6. Use Case: Xem giỏ hàng
Actor: Guest - Người dùng chưa có tài khoản, Customer - Người dùng có tài khoản, Admin - Người quản trị hệ thống

Điều kiện bắt đầu: Người dùng (Guest, Customer hoặc Admin) đang trong phiên làm việc và đã có ít nhất một sản phẩm trong giỏ hàng hoặc muốn kiểm tra giỏ hàng hiện tại.

Flow of events:

User/Customer/Admin nhấn vào biểu tượng giỏ hàng.

Phần mềm nhận yêu cầu xem giỏ hàng, truy xuất danh sách sản phẩm, số lượng và đơn giá của sản phẩm trong giỏ hàng hiện tại.

Phần mềm trả về danh sách sản phẩm trong giỏ hàng và tổng tiền.

Điều kiện kết thúc: Hệ thống hiển thị đầy đủ danh sách sản phẩm trong giỏ hàng, bao gồm tên, số lượng, đơn giá, thành tiền và tổng cộng, cho phép người dùng tiếp tục thao tác (chỉnh sửa, thanh toán, v.v.).

7. Use Case: Chỉnh số lượng sản phẩm trong giỏ hàng
Actor: Guest - Người dùng chưa có tài khoản, Customer - Người dùng có tài khoản, Admin - Người quản trị hệ thống

Điều kiện bắt đầu: Người dùng (Guest/Customer/Admin) đang ở trang giỏ hàng; sản phẩm cần chỉnh sửa đã tồn tại trong giỏ; số lượng mới là số nguyên hợp lệ (≥ 0).

Flow of events:

User/Customer/Admin thay đổi số lượng của một sản phẩm, sau đó nhấn nút cập nhật.

Phần mềm nhận số lượng mới: nếu số lượng mới là 0 thì xóa sản phẩm ra khỏi giỏ hàng, nếu số lượng > 0 thì phần mềm cập nhật lại số lượng mới trong giỏ hàng.

Phần mềm tính toán lại tổng tiền của giỏ hàng.

Phần mềm trả về trạng thái giỏ hàng đã được cập nhật.

Điều kiện kết thúc: Hệ thống cập nhật thành công số lượng (hoặc xóa sản phẩm nếu số lượng = 0), tính lại thành tiền và tổng cộng, lưu vào phiên/tài khoản và hiển thị thông báo cập nhật. Nếu số lượng không hợp lệ hoặc vượt tồn kho, hệ thống báo lỗi và không thay đổi giỏ hàng.