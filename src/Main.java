import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@SuppressWarnings("Duplicates")
public class Main extends Application {

    private Label remainingCards;
    private SorryDeck deck;
    private Label cardDescription;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // stage title
        primaryStage.setTitle("Sorry!");

        // root group
        BorderPane root = new BorderPane();

        Group menu = new Group();
        Scene startMenu = new Scene(menu, 1600,900);

        Group sorryRules = new Group();
        Scene rulesScene = new Scene(sorryRules, 1600, 900);

        makeMenu(menu, startMenu, sorryRules, rulesScene, root, primaryStage);

        //TODO:Every single time a player moves, we should remake the board
        // and have the locations of all the pawns and everything
        // PlayerBoard object, don't worry about this for now
        PlayerBoard board = new PlayerBoard(2, Color.SALMON);


        makeBoard(root, board);

        //create new Sorry! game deck
        deck = new SorryDeck();

        //shuffle the deck
        deck.shuffle();

        makeSidebar(root, deck.getTopCard());

        //Part of this function was taken from https://www.tutorialspoint.com/javafx/javafx_event_handling.htm
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

            int x, y;

            @Override
            public void handle(MouseEvent e) {

                //Calculates the coordinates of your click
                x = (int)e.getX();
                y = (int)e.getY();


                try {
                    //Moves the pawn and remakes the board
                    if (board.canMovePawn(board.getTileID(x, y), 1)) {
                        board.movePawn(board.getTileID(x, y), 1);
                    }

                    int[] bumped = board.checkSlide();
                    //print out every card
                    System.out.println(deck.getTopCard().getNumber());

                    makeBoard(root, board);
                } catch (Exception exception) {
                    System.out.println("You did not click on a board tile.");
                }
            }
        };


       root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        // this removes the pawns
        //root.getChildren().remove(boardDisplay);

        // this displays the scene with the resolution.
        primaryStage.setScene(startMenu);
        primaryStage.show();
    }

    private void makeMenu(Group menu, Scene startMenu, Group sorryRules, Scene rulesScene, BorderPane root, Stage primaryStage){
        startMenu.setFill(Color.LIGHTGREEN);
        Button startGame = new Button("Start Game");
        startGame.setTranslateX(750);
        startGame.setTranslateY(650);
        menu.getChildren().add(startGame);
        startGame.setOnMouseClicked(e -> { primaryStage.setScene(new Scene(root, 1600, 900)); });

        Button endGame = new Button("Exit");
        endGame.setTranslateX(770);
        endGame.setTranslateY(750);
        menu.getChildren().add(endGame);
        endGame.setOnMouseClicked(event ->Platform.exit());

        Button rules = new Button("How to Play");
        rules.setTranslateX(748);
        rules.setTranslateY(700);
        menu.getChildren().add(rules);
        rules.setOnMouseClicked(e -> { primaryStage.setScene(rulesScene);});

        Button back = new Button("Back to Menu");
        back.setTranslateX(745);
        back.setTranslateY(855);
        sorryRules.getChildren().add(back);
        back.setOnMouseClicked(e -> { primaryStage.setScene(startMenu);});

        Image rulesPic = new Image("/sorryRules.png", true);
        ImageView howToPlay = new ImageView(rulesPic);
        howToPlay.setFitHeight(850);
        howToPlay.setX(-200);
        sorryRules.getChildren().add(howToPlay);

        Image rulesPic2 = new Image("/sorryRules2.png", true);
        ImageView howToPlay2 = new ImageView(rulesPic2);
        howToPlay2.setFitHeight(850);
        howToPlay2.setX(750);
        sorryRules.getChildren().add(howToPlay2);

        Image background = new Image("/Sorry!.jpg", true);
        ImageView back1 = new ImageView(background);
        back1.setFitHeight(500);
        back1.setFitWidth(800);
        back1.setX(400);
        back1.setY(100);
        menu.getChildren().add(back1);
    }

    private void makeBoard(BorderPane root, PlayerBoard board) {
        // Code to create the board display, don't be afraid to put this in a function or something i'm just lazy
        Circle start1 = new Circle(325, 150, 50, Color.WHITE);
        start1.setStroke(Color.BLACK);
        Circle start2 = new Circle(800, 275, 50, Color.WHITE);
        start2.setStroke(Color.BLACK);
        Circle start3 = new Circle(675, 750, 50, Color.WHITE);
        start3.setStroke(Color.BLACK);
        Circle start4 = new Circle(200, 625, 50, Color.WHITE);
        start4.setStroke(Color.BLACK);

        Circle home1 = new Circle(225, 400, 50, Color.WHITE);
        home1.setStroke(Color.BLACK);
        Circle home2 = new Circle(550, 175, 50, Color.WHITE);
        home2.setStroke(Color.BLACK);
        Circle home3 = new Circle(775, 500, 50, Color.WHITE);
        home3.setStroke(Color.BLACK);
        Circle home4 = new Circle(450, 725, 50, Color.WHITE);
        home4.setStroke(Color.BLACK);

        root.getChildren().add(start1);
        root.getChildren().add(start2);
        root.getChildren().add(start3);
        root.getChildren().add(start4);
        root.getChildren().add(home1);
        root.getChildren().add(home2);
        root.getChildren().add(home3);
        root.getChildren().add(home4);


        for (int i = 0; i < 5; i++) {
            Rectangle square1 = new Rectangle(200, 100 + 50 * i, 50, 50);
            Rectangle square2 = new Rectangle(800 - 50 * i, 150, 50, 50);
            Rectangle square3 = new Rectangle(750, 750 - 50 * i, 50, 50);
            Rectangle square4 = new Rectangle(150 + 50 * i, 700, 50, 50);

            square1.setFill(Color.WHITE);
            square1.setStroke(Color.BLACK);
            square2.setFill(Color.WHITE);
            square2.setStroke(Color.BLACK);
            square3.setFill(Color.WHITE);
            square3.setStroke(Color.BLACK);
            square4.setFill(Color.WHITE);
            square4.setStroke(Color.BLACK);

            root.getChildren().add(square1);
            root.getChildren().add(square2);
            root.getChildren().add(square3);
            root.getChildren().add(square4);
        }

        for (int i = 0; i < 16; i++) {
            Rectangle square1 = new Rectangle(50 * i + 100, 50, 50, 50);
            Rectangle square2 = new Rectangle(50 * i + 100, 800, 50, 50);
            Rectangle square3 = new Rectangle(100, 50 * i + 50, 50, 50);
            Rectangle square4 = new Rectangle(850, 50 * i + 50, 50, 50);

            square1.setFill(Color.WHITE);
            square1.setStroke(Color.BLACK);
            square2.setFill(Color.WHITE);
            square2.setStroke(Color.BLACK);
            square3.setFill(Color.WHITE);
            square3.setStroke(Color.BLACK);
            square4.setFill(Color.WHITE);
            square4.setStroke(Color.BLACK);

            root.getChildren().add(square1);
            root.getChildren().add(square2);
            root.getChildren().add(square3);
            root.getChildren().add(square4);
        }

        // end board

        // code to display the slides

        /**
         Polygon slideArrow = new Polygon();
         slideArrow.getPoints().addAll(new Double[]{560.0, 55.0, 560.0, 95.0, 590.0, 75.0});

         Rectangle slideBody = new Rectangle(570, 70, 200, 10);

         Circle slideEnd = new Circle(775,75, 20);

         root.getChildren().add(slideBody);
         root.getChildren().add(slideEnd);
         root.getChildren().add(slideArrow);
         **/

        // this function gets the pawns to be displayed from the PlayerBoard object
        Group boardDisplay = board.displayPawns();

        // this adds the pawns to the board
        root.getChildren().add(boardDisplay);


    }

    private void makeSidebar(BorderPane root, SorryCard card) {
        Rectangle bar = new Rectangle(1000, 0, 25, 900);
        bar.setFill(Color.BLACK);

        //make a pane and place all the labels and exitbutton in it
        Pane sideBar = new Pane();

        Label label1;
        if (card.getNumber() == 0) {
            label1 = new Label("Card: Sorry!");
            label1.setTranslateY(100);
            label1.setTranslateX(1180);
            label1.setFont(new Font("Times New Roman", 30));
            root.getChildren().add(label1);
        } else {
            label1 = new Label("Card: " + card.getNumber());
            label1.setTranslateY(100);
            label1.setTranslateX(1180);
            label1.setFont(new Font("Times New Roman", 30));
            root.getChildren().add(label1);
        }

        cardDescription = new Label("Description: " + card.getDescription());
        cardDescription.setTranslateY(150);
        cardDescription.setTranslateX(1050);
        cardDescription.setMaxWidth(375);
        cardDescription.setFont(new Font("Times New Roman", 20));

        remainingCards =  new Label("Cards remaining in deck: ");
        remainingCards.setTranslateY(835);
        remainingCards.setTranslateX(1050);
        remainingCards.setMaxWidth(375);
        remainingCards.textProperty().bind(Bindings.concat("Cards left: ").concat(new SimpleIntegerProperty(deck.cardsRemaining()).asString()));

        sideBar.getChildren().add(bar);
        sideBar.getChildren().add(label1);
        sideBar.getChildren().add(cardDescription);
        sideBar.getChildren().add(remainingCards);

        //create Button to exit the game
        Button exitButton = new Button("Exit Game");
        HBox hbButton = new HBox();
        hbButton.getChildren().add(exitButton);
        hbButton.setAlignment(Pos.CENTER_RIGHT);
        exitButton.setOnAction(event ->Platform.exit());

        BorderPane theButton = new BorderPane();
        theButton.setPrefSize(1600, 900);
        theButton.setPadding(new Insets(830, 350, 0, 10));
        
        theButton.setRight(exitButton);
        theButton.getChildren().add(hbButton);
        sideBar.getChildren().add(theButton);



        root.getChildren().add(sideBar);

       /* root.getChildren().add(bar);
        root.getChildren().add(cardDescription);
        root.getChildren().add(remainingCards);*/



       // root.setTop(sideBar);
    }



    public static void main(String[] args) {
        Application.launch(args);
    }


}
