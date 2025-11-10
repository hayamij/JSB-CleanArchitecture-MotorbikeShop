package com.motorbike.main;

import com.motorbike.main.business.usecase.ListOrdersControl;
import com.motorbike.main.business.usecase.ListOrdersInputBoundary;
import com.motorbike.main.persistence.MockOrderGateway;
import com.motorbike.main.persistence.OrderGateway;
import com.motorbike.main.presentation.OrderListController;
import com.motorbike.main.presentation.OrderListPresenter;
import com.motorbike.main.presentation.OrderListViewFX;
import com.motorbike.main.presentation.OrderListViewModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // === Wiring các tầng (theo Clean Architecture) ===
        OrderGateway gateway = new MockOrderGateway();
        OrderListViewModel viewModel = new OrderListViewModel();
        OrderListPresenter presenter = new OrderListPresenter(viewModel);
        ListOrdersInputBoundary useCase = new ListOrdersControl(gateway, presenter);
        OrderListController controller = new OrderListController(useCase);

        // === Load FXML ===
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderListView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Order Manager");
        stage.setScene(scene);

        // === Inject dependencies vào FX Controller ===
        OrderListViewFX fxController = loader.getController();
        fxController.setController(controller);
        fxController.setViewModel(viewModel);
        fxController.bindViewModel(); // đăng ký listener sau khi set ViewModel

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
