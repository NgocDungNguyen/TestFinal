package com.restaurant.gui;

import com.restaurant.entity.Customer;
import com.restaurant.service.CustomerService;
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
public class CustomerView {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TextField nameField, addressField, phoneField, searchField;
    @FXML
    private Pagination pagination;

    @Autowired
    private CustomerService customerService;

    private int pageSize = 20;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";

    @FXML
    public void initialize() {
        setupTable();
        setupPagination();
        refreshTable();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        idColumn.sortableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sortBy = "id";
                sortDirection = idColumn.getSortType() == TableColumn.SortType.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                refreshTable();
            }
        });
    }

    private void setupPagination() {
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Customer> createPage(int pageIndex) {
        refreshTable(pageIndex);
        return customerTable;
    }

    @FXML
    private void handleAddCustomer() {
        Customer customer = new Customer(nameField.getText(), addressField.getText(), phoneField.getText());
        customerService.addCustomer(customer);
        clearFields();
        refreshTable();
    }

    @FXML
    private void handleUpdateCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            selectedCustomer.setName(nameField.getText());
            selectedCustomer.setAddress(addressField.getText());
            selectedCustomer.setPhoneNumber(phoneField.getText());
            customerService.updateCustomer(selectedCustomer);
            clearFields();
            refreshTable();
        } else {
            showAlert("No Customer Selected", "Please select a customer to update.");
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            customerService.deleteCustomer(selectedCustomer.getId());
            refreshTable();
        } else {
            showAlert("No Customer Selected", "Please select a customer to delete.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        Page<Customer> customerPage = customerService.searchCustomers(searchTerm, PageRequest.of(0, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(customerPage);
    }

    private void refreshTable() {
        refreshTable(0);
    }

    private void refreshTable(int pageIndex) {
        Page<Customer> customerPage = customerService.getAllCustomers(PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(customerPage);
    }

    private void updateTableAndPagination(Page<Customer> customerPage) {
        customerTable.setItems(FXCollections.observableArrayList(customerPage.getContent()));
        pagination.setPageCount(customerPage.getTotalPages());
        pagination.setCurrentPageIndex(customerPage.getNumber());
    }

    private void clearFields() {
        nameField.clear();
        addressField.clear();
        phoneField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}