package com.restaurant.gui;

import com.restaurant.entity.Deliveryman;
import com.restaurant.service.DeliverymanService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import com.restaurant.entity.Customer;
import com.restaurant.entity.Item;
import com.restaurant.entity.Order;
import com.restaurant.entity.Deliveryman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Component
public class DeliverymanView {

    @FXML
    private TableView<Deliveryman> deliverymanTable;
    @FXML
    private TableColumn<Deliveryman, Integer> idColumn;
    @FXML
    private TableColumn<Deliveryman, String> nameColumn;
    @FXML
    private TableColumn<Deliveryman, String> phoneNumberColumn;
    @FXML
    private TextField nameField, phoneNumberField, searchField;
    @FXML
    private Pagination pagination;

    @Autowired
    private DeliverymanService deliverymanService;

    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";

    @FXML
    public void initialize() {
        setupTable();
        setupPagination();
        loadDeliverymen();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        idColumn.sortableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sortBy = "id";
                sortDirection = idColumn.getSortType() == TableColumn.SortType.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                loadDeliverymen(); // or loadDeliverymen() or loadOrders() or loadCustomers() depending on the view
            }
        });
    }

    private void setupPagination() {
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Deliveryman> createPage(int pageIndex) {
        loadDeliverymen(pageIndex);
        return deliverymanTable;
    }

    @FXML
    private void handleAddDeliveryman() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        Deliveryman deliveryman = new Deliveryman(name, phoneNumber);
        deliverymanService.addDeliveryman(deliveryman);
        clearFields();
        loadDeliverymen();
    }

    @FXML
    private void handleUpdateDeliveryman() {
        Deliveryman selectedDeliveryman = deliverymanTable.getSelectionModel().getSelectedItem();
        if (selectedDeliveryman != null) {
            selectedDeliveryman.setName(nameField.getText());
            selectedDeliveryman.setPhoneNumber(phoneNumberField.getText());
            deliverymanService.updateDeliveryman(selectedDeliveryman);
            clearFields();
            loadDeliverymen();
        } else {
            showAlert("No Selection", "Please select a deliveryman to update.");
        }
    }

    @FXML
    private void handleDeleteDeliveryman() {
        Deliveryman selectedDeliveryman = deliverymanTable.getSelectionModel().getSelectedItem();
        if (selectedDeliveryman != null) {
            deliverymanService.deleteDeliveryman(selectedDeliveryman.getId());
            loadDeliverymen();
        } else {
            showAlert("No Selection", "Please select a deliveryman to delete.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        Page<Deliveryman> deliverymanPage = deliverymanService.searchDeliverymen(searchTerm, PageRequest.of(0, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(deliverymanPage);
    }

    private void loadDeliverymen() {
        loadDeliverymen(0);
    }

    private void loadDeliverymen(int pageIndex) {
        Page<Deliveryman> deliverymanPage = deliverymanService.getAllDeliverymen(PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(deliverymanPage);
    }

    private void updateTableAndPagination(Page<Deliveryman> deliverymanPage) {
        deliverymanTable.setItems(FXCollections.observableArrayList(deliverymanPage.getContent()));
        pagination.setPageCount(deliverymanPage.getTotalPages());
        pagination.setCurrentPageIndex(deliverymanPage.getNumber());
    }

    private void clearFields() {
        nameField.clear();
        phoneNumberField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}