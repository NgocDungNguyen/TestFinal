package com.restaurant.gui;

import com.restaurant.entity.Item;
import com.restaurant.service.ItemService;
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
public class ItemView {

    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, Integer> idColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, Double> priceColumn;
    @FXML
    private TextField nameField, priceField, searchField;
    @FXML
    private Pagination pagination;

    @Autowired
    private ItemService itemService;

    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";

    @FXML
    public void initialize() {
        setupTable();
        setupPagination();
        loadItems();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        idColumn.sortableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sortBy = "id";
                sortDirection = idColumn.getSortType() == TableColumn.SortType.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                loadItems(); // or loadDeliverymen() or loadOrders() or loadCustomers() depending on the view
            }
        });
    }

    private void setupPagination() {
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Item> createPage(int pageIndex) {
        loadItems(pageIndex);
        return itemTable;
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = nameField.getText();
            Double price = Double.parseDouble(priceField.getText());
            Item item = new Item(name, price);
            itemService.addItem(item);
            clearFields();
            loadItems();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for price.");
        }
    }

    @FXML
    private void handleUpdateItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                selectedItem.setName(nameField.getText());
                selectedItem.setPrice(Double.parseDouble(priceField.getText()));
                itemService.updateItem(selectedItem);
                clearFields();
                loadItems();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for price.");
            }
        } else {
            showAlert("No Selection", "Please select an item to update.");
        }
    }

    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            itemService.deleteItem(selectedItem.getId());
            loadItems();
        } else {
            showAlert("No Selection", "Please select an item to delete.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        Page<Item> itemPage = itemService.searchItemsByName(searchTerm, PageRequest.of(0, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(itemPage);
    }

    private void loadItems() {
        loadItems(0);
    }

    private void loadItems(int pageIndex) {
        Page<Item> itemPage = itemService.getAllItems(PageRequest.of(pageIndex, pageSize, Sort.by(sortDirection, sortBy)));
        updateTableAndPagination(itemPage);
    }

    private void updateTableAndPagination(Page<Item> itemPage) {
        itemTable.setItems(FXCollections.observableArrayList(itemPage.getContent()));
        pagination.setPageCount(itemPage.getTotalPages());
        pagination.setCurrentPageIndex(itemPage.getNumber());
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}