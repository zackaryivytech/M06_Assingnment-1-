package com.example.m06_programming1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;

import javafx.scene.layout.VBox;

import java.io.IOException;


public class HelloApplication extends Application {


    //Database Log in Info
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/staff_assignment1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    //Labels
    Label idLabel;
    Label lastNameLabel;
    Label firstNameLabel;
    Label miLabel;
    Label addressLabel;
    Label cityLabel;
    Label stateLabel;
    Label telephoneLabel;

    //Text Fields
    TextField idTextField;
    TextField lastNameTextField;
    TextField firstNameTextField;
    TextField miTextField;
    TextField addressTextField;
    TextField cityTextField;
    TextField stateTextField;
    TextField telephoneTextField;


    @Override
    public void start(Stage stage) throws IOException, SQLException {

        Connection connection = null;

        //Try to create connection with database
        try{
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        //Horizontal Boxes
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        HBox hbox6 = new HBox();


        //------------------------------------
        idLabel = new Label("ID");

        idTextField = new TextField();
        idTextField.setMaxWidth(100);

        //------------------------------------
        hbox1.getChildren().add(idLabel);
        hbox1.getChildren().add(idTextField);


        lastNameLabel = new Label("Last Name");

        lastNameTextField = new TextField();
        lastNameTextField.setMaxWidth(125);

        hbox2.getChildren().add(lastNameLabel);
        hbox2.getChildren().add(lastNameTextField);

        //------------------------------------
        firstNameLabel = new Label("First Name");

        firstNameTextField = new TextField();
        firstNameTextField.setMaxWidth(125);

        hbox2.getChildren().add(firstNameLabel);
        hbox2.getChildren().add(firstNameTextField);


        //------------------------------------

        miLabel = new Label("MI");

        miTextField = new TextField();
       // miTextField.setMax
        miTextField.setMaxWidth(25);

        hbox2.getChildren().add(miLabel);
        hbox2.getChildren().add(miTextField);

        //------------------------------------

        addressLabel = new Label("Address");

        addressTextField = new TextField();
        addressTextField.setMaxWidth(145);

        hbox3.getChildren().add(addressLabel);
        hbox3.getChildren().add(addressTextField);

        //------------------------------------

        cityLabel = new Label("City");

        cityTextField = new TextField();
        cityTextField.setMaxWidth(145);

        hbox4.getChildren().add(cityLabel);
        hbox4.getChildren().add(cityTextField);

        //------------------------------------

        stateLabel = new Label("State");

        stateTextField = new TextField();
        stateTextField.setMaxWidth(145);

        hbox4.getChildren().add(stateLabel);
        hbox4.getChildren().add(stateTextField);

        //------------------------------------

        telephoneLabel = new Label("Telephone");

        telephoneTextField = new TextField();
        telephoneTextField.setMaxWidth(145);

        hbox5.getChildren().add(telephoneLabel);
        hbox5.getChildren().add(telephoneTextField);

        //------------------------------------


        //Buttons
        Button viewButton = new Button("View");
        Button insertButton = new Button("Insert");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");

        //Button Events
        Connection finalConnection = connection;
        viewButton.setOnAction(e ->  {viewButton(finalConnection);});
        insertButton.setOnAction(e ->  {
            try {
                insertButton(finalConnection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateButton.setOnAction(e ->  {
            try {
                updateButton(finalConnection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        clearButton.setOnAction(e ->  {clearButton();});


        //Add Buttons
        hbox6.getChildren().add(viewButton);
        hbox6.getChildren().add(insertButton);
        hbox6.getChildren().add(updateButton);
        hbox6.getChildren().add(clearButton);
        hbox6.setAlignment(Pos.CENTER);

        //Add HBoxes
        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);

        Pane mainPane = new Pane();
        mainPane.getChildren().add(vbox);

        Scene scene = new Scene(mainPane, 470, 200);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    //Gets data from database using ID and displays it into text fields
    public void viewButton(Connection connection){
        int id;

        //Try to get int from id text field
        try {
            id = Integer.parseInt(idTextField.getText());
        }
        catch(Exception e){
            System.out.println("ERROR: Invalid ID");
            return;
        }

        //Select query
        String query = "SELECT * FROM staff WHERE id = " + id;

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){

                lastNameTextField.setText(resultSet.getString("lastName"));
                firstNameTextField.setText(resultSet.getString("firstName"));
                miTextField.setText(resultSet.getString("mi"));
                addressTextField.setText(resultSet.getString("address"));
                cityTextField.setText(resultSet.getString("city"));
                stateTextField.setText(resultSet.getString("state"));
                telephoneTextField.setText(resultSet.getString("telephone"));


            }else{
                System.out.println("ERROR: Person not found");
            }

        }
        catch(Exception e){


        }
    }

    /**
     * Inserts a new entry into the database
     */
    public void insertButton(Connection connection) throws SQLException {

        int id = 0;

        //Get ID
        try {
            id = Integer.parseInt(idTextField.getText().replaceAll("\\D", ""));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Insert query
        String query = "INSERT INTO staff (id, lastName, firstName, mi, address, city, state, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, id);
            statement.setString(2, lastNameTextField.getText());
            statement.setString(3, firstNameTextField.getText());
            statement.setString(4, miTextField.getText());
            statement.setString(5, addressTextField.getText());
            statement.setString(6, cityTextField.getText());
            statement.setString(7, stateTextField.getText());
            statement.setString(8, telephoneTextField.getText());

            statement.executeUpdate();
        }
    }

    /**
     * Updates an entry in the database
     *  */
    public void updateButton(Connection connection) throws SQLException {

        int id = 0;

        //Get ID
        try {
            id = Integer.parseInt(idTextField.getText().replaceAll("\\D", ""));
        }
        catch(Exception e){
            e.printStackTrace();
        }


        //Update Query
        String query = "UPDATE staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ? WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){


            statement.setString(1, lastNameTextField.getText());
            statement.setString(2, firstNameTextField.getText());
            statement.setString(3, miTextField.getText());
            statement.setString(4, addressTextField.getText());
            statement.setString(5, cityTextField.getText());
            statement.setString(6, stateTextField.getText());
            statement.setString(7, telephoneTextField.getText());
            statement.setInt(8, id);

            statement.executeUpdate();
        }


    }

    /**
     * Clears all text fields in GUI
     * */
    public void clearButton(){

        //Clears all text fields
        idTextField.clear();
        lastNameTextField.clear();
        firstNameTextField.clear();
        miTextField.clear();
        addressTextField.clear();
        cityTextField.clear();
        stateTextField.clear();
        telephoneTextField.clear();



    }

    public static void main(String[] args) {
        launch();
    }
}