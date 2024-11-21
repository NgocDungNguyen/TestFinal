package com.restaurant.gui;

import com.restaurant.entity.Customer;
import com.restaurant.entity.Deliveryman;
import com.restaurant.entity.Order;
import com.restaurant.service.CustomerService;
import com.restaurant.service.DeliverymanService;
import com.restaurant.service.OrderService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderView {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    private TableColumn<Order, String> customerColumn;
    @FXML
    private TableColumn<Order, String> deliverymanColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableColumn<Order, LocalDate> creationDateColumn;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Deliveryman> deliverymanComboBox;
    @FXML
    private TextField totalPriceField, searchField;
    @FXML
    private Pagination pagination;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeliverymanService deliverymanService;

    private int pageSize = 20;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";

    @FXML
    public void initialize() {
        setupTable();
        setupPagination();
        loadCustomersAndDeliverymen();
        refreshTable();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return new SimpleStringProperty(customer != null ? customer.getName() : "");
        });
        deliverymanColumn.setCellValueFactory(cellData -> {
            Deliveryman deliveryman = cellData.getValue().getDeliveryman();
            return new SimpleStringProperty(deliveryman != null ? deliveryman.getName() : "");
        });
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        // Add sorting
        idColumn.setSortable(true);
        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        idColumn.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
            sortDirection = (newValue == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            refreshTable();
        });
    }


    private void setupPagination() {
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Order> createPage(int pageIndex) {
        refreshTable(pageIndex);
        return orderTable;
    }

    private void loadCustomersAndDeliverymen() {
        customerComboBox.setItems(FXCollections.observableArrayList(customerService.getAllCustomers(PageRequest.of(0, Integer.MAX_VALUE)).getContent()));
        deliverymanComboBox.setItems(FXCollections.observableArrayList(deliverymanService.getAllDeliverymen(PageRequest.of(0, Integer.MAX_VALUE)).getContent()));
    }

    @FXML
    private void handleAddOrder() {
        try {
            Customer customer = customerComboBox.getValue();
            Deliveryman deliveryman = deliverymanComboBox.getValue();
            Double totalPrice = Double.parseDouble(totalPriceField.getText());

            if (customer == null || deliveryman == null) {
                showAlert("Missing Information", "Please select a customer and a deliveryman.");
                return;
            }

            Order order = new Order(totalPrice);
            order.setCustomer(customer);
            order.setDeliveryman(deliveryman);
            orderService.addOrder(order);
            clearFields();
            refreshTable();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the total price.");
        }
    }

    @FXML
    private void handleUpdateOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                Customer customer = customerComboBox.getValue();
                Deliveryman deliveryman = deliverymanComboBox.getValue();
                Double totalPrice = Double.parseDouble(totalPriceField.getText());

                if (customer == null || deliveryman == null) {
                    showAlert("Missing Information", "Please select a customer and a deliveryman.");
                    return;
                }

                selectedOrder.setCustomer(customer);
                selectedOrder.setDeliveryman(deliveryman);
                selectedOrder.setTotalPrice(totalPrice);
                orderService.updateOrder(selectedOrder);
                clearFields();
                refreshTable();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for the total price.");
            }
        } else {
            showAlert("No Order Selected", "Please select an order to update.");
        }
    }

    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderService.deleteOrder(selectedOrder.getId());
            refreshTable();
        } else {
            showAlert("No Order Selected", "Please select an order to delete.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        Page<Order> orderPage = orderService.searchOrders(searchTerm, PageRequest.of(0, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(orderPage);
    }

    private void refreshTable() {
        refreshTable(0);
    }

    private void refreshTable(int pageIndex) {
        Page<Order> orderPage = orderService.getAllOrders(PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(orderPage);
    }

    private void updateTableAndPagination(Page<Order> orderPage) {
        orderTable.setItems(FXCollections.observableArrayList(orderPage.getContent()));
        pagination.setPageCount(orderPage.getTotalPages());
        pagination.setCurrentPageIndex(orderPage.getNumber());
    }

    private void clearFields() {
        customerComboBox.getSelectionModel().clearSelection();
        deliverymanComboBox.getSelectionModel().clearSelection();
        totalPriceField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}