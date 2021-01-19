package foobar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class Main extends Application {
    private GridPane grid;
    private Label text;
    private MenuBar mb;
    private Menu menu;
    private Menu options;
    private VBox root;

    private void errorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Это бан");
        alert.setHeaderText(null);
        alert.setContentText("Вы ввели что-то не то и все поломалось.\n" +
                "Мои поздравления.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            Platform.exit();
            System.exit(0);
        }
        alert.showAndWait();
    }

    private File fileDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        return fileChooser.showOpenDialog(primaryStage);
    }

    private void themeDialog() {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Белый");
        choices.add("Серый");
        choices.add("Сине-зеленый");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Вы выбираете");
        dialog.setHeaderText("Выбор заднего фона приложения");
        dialog.setContentText("Выберете нужный вам вариант:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")) {
            if (result.get().equals("Белый")) {
                root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (result.get().equals("Серый")) {
                root.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                root.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void buttonDialog() {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Белые кнопки");
        choices.add("Инверированный цвет");
        choices.add("Кислотный вариант");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Вы выбираете");
        dialog.setHeaderText("Выбор стиля кнопок");
        dialog.setContentText("Выберете нужный вам вариант:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")) {
            if (result.get().equals("Белые кнопки")) {
                menu.setStyle("-fx-font-size:12; -fx-background-color: white; -fx-text-fill: black");
                options.setStyle("-fx-font-size:12; -fx-background-color: white; -fx-text-fill: black");
                mb.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (result.get().equals("Инверированный цвет")) {
                menu.setStyle("-fx-font-size:12; -fx-background-color: black; -fx-text-fill: white");
                options.setStyle("-fx-font-size:12; -fx-background-color: black; -fx-text-fill: white");
                mb.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                menu.setStyle("-fx-font-size:12; -fx-background-color: green; -fx-text-fill: red");
                options.setStyle("-fx-font-size:12; -fx-background-color: green; -fx-text-fill: red");
                mb.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void createGrid(Image img) {
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setGridLinesVisible(false);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(new ImageView(img), 0, 0, 2, 1);
        grid.add(text, 0, 1, 2, 1);

    }

    private void createMB(Stage stg) {
        MenuItem toCae = new MenuItem("Зашифровать");
        toCae.setOnAction((ActionEvent event) -> {
            try {
                String foo = Files.readString(fileDialog(stg).toPath());
                foo = cipher(foo, 1);
                text.setText(foo);
            } catch (Exception e) {
                errorDialog();
            }
        });
        MenuItem fromCae = new MenuItem("Расшифровать");
        fromCae.setOnAction((ActionEvent event) -> {
            try {
                String foo = Files.readString(fileDialog(stg).toPath());
                foo = cipher(foo, -1);
                text.setText(foo);
            } catch (Exception e) {
                errorDialog();
            }
        });
        menu = new Menu("Меню");
        menu.getItems().addAll(toCae, fromCae);

        MenuItem normal = new MenuItem("Сменить цвет фона");
        normal.setOnAction((ActionEvent event) -> themeDialog());
        MenuItem invert = new MenuItem("Сменить стиль кнопок");
        invert.setOnAction((ActionEvent event) -> buttonDialog());
        options = new Menu("Опции");
        options.getItems().addAll(normal, invert);

        mb = new MenuBar();
        mb.getMenus().addAll(menu, options);
    }

    private String cipher(String msg, int shift) {
        StringBuilder s = new StringBuilder();
        char c;
        int len = msg.length();
        for (int x = 0; x < len; x++) {
            if ((msg.charAt(x) >= 'a' && msg.charAt(x) <= 'z') || (msg.charAt(x) >= 'A' && msg.charAt(x) <= 'Z')) {
                if (shift > 0) {
                    c = (char) (msg.charAt(x) + shift);
                    if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')))
                        s.append((char) (msg.charAt(x) - (26 - shift)));
                    else
                        s.append(c);
                } else {
                    c = (char) (msg.charAt(x) + shift);
                    if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')))
                        s.append((char) (msg.charAt(x) + (26 + shift)));
                    else
                        s.append(c);
                }
            } else {
                s.append(msg.charAt(x));
            }
        }
        return s.toString();
    }

    @Override
    public void start(Stage primaryStage) {
        text = new Label("");
        Image img = new Image(Main.class.getResourceAsStream("images/img.png"));
        grid = new GridPane();
        createGrid(img);

        createMB(primaryStage);

        root = new VBox(mb);
        root.getChildren().add(grid);
        root.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setTitle("Шпиён - программа");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
