package com.motorbike.main.presentation;

import com.motorbike.main.shared.OrderDTO;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrderListViewFX {

    @FXML private TableView<OrderDTO> tblOrders;
    @FXML private TableColumn<OrderDTO, Number> colId;
    @FXML private TableColumn<OrderDTO, Number> colCustomer;
    @FXML private TableColumn<OrderDTO, String> colStatus;
    @FXML private TableColumn<OrderDTO, String> colDate;

    private OrderListController controller;
    private OrderListViewModel viewModel;

    // Inject từ MainApp
    public void setController(OrderListController controller) {
        this.controller = controller;
    }

    public void setViewModel(OrderListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getMaDonHang()));
        colCustomer.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getMaKhachHang()));
        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTrangThai()));
        colDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getNgayDatHang().toString()));
    }

    // Sau khi viewModel được set từ MainApp, đăng ký listener để cập nhật TableView
    public void bindViewModel() {
        if (viewModel != null) {
            viewModel.addSubscriber(() -> {
                // Lấy danh sách OrderDTO từ ViewModel và hiển thị lên TableView
                tblOrders.setItems(FXCollections.observableArrayList(viewModel.getOrders()));
            });
        }
    }

    @FXML
    public void onViewOrders() {
        if (controller != null)
            controller.execute();
    }
}
