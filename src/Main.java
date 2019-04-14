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
import java.util.Random;

@SuppressWarnings("Duplicates")
public class Main extends Application {

    private Label remainingCards;
    private SorryDeck deck;
    private Label cardDescription;
    private Label cardNumber;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // stage title
        primaryStage.setTitle("Sorry!");

        //create an instance of the game
        deck = new SorryDeck();

        deck.shuffle();

        // root group
        BorderPane root = new BorderPane();

        Group menu = new Group();
        Scene startMenu = new Scene(menu, 1450, 900);

        Group sorryRules = new Group();
        Scene rulesScene = new Scene(sorryRules, 1450, 900);

        makeMenu(menu, startMenu, sorryRules, rulesScene, root, primaryStage);

        //TODO:Every single time a player moves, we should remake the board
        // and have the locations of all the pawns and everything
        // PlayerBoard object, don't worry about this for now
        PlayerBoard board = new PlayerBoard(0, Color.RED);


        makeBoard(root, board);

        //create new Sorry! game deck
        makeSidebar(root, deck.getTopCard());

        // this displays the scene with the resolution.
        primaryStage.setScene(startMenu);
        primaryStage.show();

        //Choose first player to go
        /*
        Random rand = new Random();
        int firstPlayer = rand.nextInt(4);
        if (firstPlayer == 0){

        }
        */

        //Part of this function was taken from https://www.tutorialspoint.com/javafx/javafx_event_handling.htm
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

            int x, y;

            @Override
            public void handle(MouseEvent e) {

                //Calculates the coordinates of your click
                x = (int) e.getX();
                y = (int) e.getY();


                    try {
                        //Moves the pawn and remakes the board
                        if (board.canMovePawn(board.getTileID(x, y), -1)) {
                            board.movePawn(board.getTileID(x, y), -1);
                        }


                    int[] bumped = board.checkSlide();
                    //print out every card
                    System.out.println(deck.getTopCard().getNumber());

                    makeBoard(root, board);
                    makeSidebar(root, deck.getTopCard());

                } catch (Exception exception) {
                    System.out.println("You did not click on a board tile.");
                }
            }
        };


        root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        // this removes the pawns
        //root.getChildren().remove(boardDisplay);

    }

    private void makeMenu(Group menu, Scene startMenu, Group sorryRules, Scene rulesScene, BorderPane root, Stage primaryStage) {
        startMenu.setFill(Color.LIGHTGREEN);
        Button startGame = new Button("Start Game");
        startGame.setTranslateX(690);
        startGame.setTranslateY(650);
        menu.getChildren().add(startGame);
        startGame.setOnMouseClicked(e -> {
            primaryStage.setScene(new Scene(root, 1450, 900));
        });

        Button endGame = new Button("Exit");
        endGame.setTranslateX(710);
        endGame.setTranslateY(750);
        menu.getChildren().add(endGame);
        endGame.setOnMouseClicked(event -> Platform.exit());

        Button rules = new Button("How to Play");
        rules.setTranslateX(689);
        rules.setTranslateY(700);
        menu.getChildren().add(rules);
        rules.setOnMouseClicked(e -> {
            primaryStage.setScene(rulesScene);
        });

        Button back = new Button("Back to Menu");
        back.setTranslateX(655);
        back.setTranslateY(855);
        sorryRules.getChildren().add(back);
        back.setOnMouseClicked(e -> {
            primaryStage.setScene(startMenu);
        });

        Image rulesPic = new Image("/sorryRules.png", true);
        ImageView howToPlay = new ImageView(rulesPic);
        howToPlay.setFitHeight(850);
        howToPlay.setFitWidth(700);
        howToPlay.setX(0);

        Image rulesPic2 = new Image("/sorryRules2.png", true);
        ImageView howToPlay2 = new ImageView(rulesPic2);
        howToPlay2.setFitHeight(850);
        howToPlay2.setFitWidth(700);
        howToPlay2.setX(700);
        sorryRules.getChildren().add(howToPlay2);
        sorryRules.getChildren().add(howToPlay);

        Image background = new Image("/Sorry!.jpg", true);
        ImageView back1 = new ImageView(background);
        back1.setFitHeight(500);
        back1.setFitWidth(800);
        back1.setX(325);
        back1.setY(100);
        menu.getChildren().add(back1);
    }

    private void makeBoard(BorderPane root, PlayerBoard board) {

        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,  CornerRadii.EMPTY, Insets.EMPTY)));
   /*     HBox SorryBox = new HBox();
        //SorryBox.setAlignment(Pos.CENTER);
        Label SorryLabel = new Label("Sorry!");
        SorryLabel.setFont(new Font("Times New Roman", 30));
        SorryBox.setTranslateX(700);
        SorryBox.setTranslateY(450);
        SorryBox.getChildren().add(SorryLabel);
        root.getChildren().add(SorryBox);
*/
        // Code to create the board display, don't be afraid to put this in a function or something i'm just lazy
        Circle start1 = new Circle(325, 150, 50, Color.RED);
        start1.setStroke(Color.BLACK);
        Circle start2 = new Circle(800, 275, 50, Color.BLUE);
        start2.setStroke(Color.BLACK);
        Circle start3 = new Circle(675, 750, 50, Color.YELLOW);
        start3.setStroke(Color.BLACK);
        Circle start4 = new Circle(200, 625, 50, Color.GREEN);
        start4.setStroke(Color.BLACK);

        Circle home1 = new Circle(225, 400, 50, Color.RED);
        home1.setStroke(Color.BLACK);
        Circle home2 = new Circle(550, 175, 50, Color.BLUE);
        home2.setStroke(Color.BLACK);
        Circle home3 = new Circle(775, 500, 50, Color.YELLOW);
        home3.setStroke(Color.BLACK);
        Circle home4 = new Circle(450, 725, 50, Color.GREEN);
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

            square1.setFill(Color.RED);
            square1.setStroke(Color.BLACK);
            square2.setFill(Color.BLUE);
            square2.setStroke(Color.BLACK);
            square3.setFill(Color.YELLOW);
            square3.setStroke(Color.BLACK);
            square4.setFill(Color.GREEN);
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

        Polygon slideArrow = new Polygon();

        slideArrow.getPoints().addAll(560.0, 55.0, 560.0, 95.0, 590.0, 75.0);
        slideArrow.setFill(Color.RED);

        slideArrow.setStroke(Color.RED);
        slideArrow.setStrokeWidth(3.0);

        Polygon slideBody = new Polygon();

        slideBody.getPoints().addAll(570.0, 70.0, 570.0, 80.0, 770.0, 80.0, 770.0, 70.0);
        slideBody.setFill(Color.RED);

        slideBody.setStroke(Color.RED);
        slideBody.setStrokeWidth(3.0);

        Circle slideEnd = new Circle(775, 75, 20);
        slideEnd.setFill(Color.RED);
        slideEnd.setStroke(Color.RED);
        slideEnd.setStrokeWidth(3.0);

        root.getChildren().add(slideBody);
        root.getChildren().add(slideEnd);
        root.getChildren().add(slideArrow);

        Polygon slideArrow2 = new Polygon();
      
        slideArrow2.getPoints().addAll(160.0, 55.0, 160.0, 95.0, 190.0, 75.0);
        slideArrow2.setFill(Color.RED);

        slideArrow2.setStroke(Color.RED);
        slideArrow2.setStrokeWidth(3.0);

        Polygon slideBody2 = new Polygon();
      
        slideBody2.getPoints().addAll(170.0, 70.0, 170.0, 80.0, 320.0, 80.0, 320.0, 70.0);
        slideBody2.setFill(Color.RED);

        slideBody2.setStroke(Color.RED);
        slideBody2.setStrokeWidth(3.0);

        Circle slideEnd2 = new Circle(325, 75, 20);
        slideEnd2.setFill(Color.RED);
        slideEnd2.setStroke(Color.RED);
        slideEnd2.setStrokeWidth(3.0);

        root.getChildren().add(slideBody2);
        root.getChildren().add(slideEnd2);
        root.getChildren().add(slideArrow2);

        Polygon slideArrow3 = new Polygon();

        slideArrow3.getPoints().addAll(855.0, 105.0, 895.0, 105.0, 875.0, 140.0);
        slideArrow3.setFill(Color.BLUE);
        slideArrow3.setStroke(Color.BLUE);
        slideArrow3.setStrokeWidth(3.0);

        Polygon slideBody3 = new Polygon();
        slideBody3.getPoints().addAll(870.0, 120.0, 870.0, 270.0, 880.0, 270.0, 880.0, 120.0);
        slideBody3.setFill(Color.BLUE);
        slideBody3.setStroke(Color.BLUE);
        slideBody3.setStrokeWidth(3.0);

        Circle slideEnd3 = new Circle(875, 275, 20);
        slideEnd3.setFill(Color.BLUE);
        slideEnd3.setStroke(Color.BLUE);
        slideEnd3.setStrokeWidth(3.0);

        root.getChildren().add(slideBody3);
        root.getChildren().add(slideEnd3);
        root.getChildren().add(slideArrow3);

        Polygon slideArrow4 = new Polygon();

        slideArrow4.getPoints().addAll(855.0, 505.0, 895.0, 505.0, 875.0, 540.0);
        slideArrow4.setFill(Color.BLUE);
        slideArrow4.setStroke(Color.BLUE);
        slideArrow4.setStrokeWidth(3.0);

        Polygon slideBody4 = new Polygon();
        slideBody4.getPoints().addAll(870.0, 520.0, 870.0, 720.0, 880.0, 720.0, 880.0, 520.0);
        slideBody4.setFill(Color.BLUE);
        slideBody4.setStroke(Color.BLUE);
        slideBody4.setStrokeWidth(3.0);

        Circle slideEnd4 = new Circle(875, 725, 20);
        slideEnd4.setFill(Color.BLUE);
        slideEnd4.setStroke(Color.BLUE);
        slideEnd4.setStrokeWidth(3.0);

        root.getChildren().add(slideBody4);
        root.getChildren().add(slideEnd4);
        root.getChildren().add(slideArrow4);

        Polygon slideArrow5 = new Polygon();

        slideArrow5.getPoints().addAll(845.0, 805.0, 845.0, 845.0, 810.0, 825.0);
        slideArrow5.setFill(Color.YELLOW);
        slideArrow5.setStroke(Color.YELLOW);
        slideArrow5.setStrokeWidth(new Double(3.0));

        Polygon slideBody5 = new Polygon();
        slideBody5.getPoints().addAll(820.0, 820.0, 670.0, 820.0, 670.0, 830.0, 820.0, 830.0);
        slideBody5.setFill(Color.YELLOW);
        slideBody5.setStroke(Color.YELLOW);
        slideBody5.setStrokeWidth(3.0);

        Circle slideEnd5 = new Circle(675, 825, 20);
        slideEnd5.setFill(Color.YELLOW);
        slideEnd5.setStroke(Color.YELLOW);
        slideEnd5.setStrokeWidth(3.0);

        root.getChildren().add(slideBody5);
        root.getChildren().add(slideEnd5);
        root.getChildren().add(slideArrow5);

        Polygon slideArrow6 = new Polygon();

         slideArrow6.getPoints().addAll(445.0, 805.0, 445.0, 845.0, 410.0, 825.0);
        slideArrow6.setFill(Color.YELLOW);
        slideArrow6.setStroke(Color.YELLOW);
        slideArrow6.setStrokeWidth(new Double(3.0));

        Polygon slideBody6 = new Polygon();
        slideBody6.getPoints().addAll(420.0, 820.0, 220.0, 820.0, 220.0, 830.0, 420.0, 830.0);
        slideBody6.setFill(Color.YELLOW);
        slideBody6.setStroke(Color.YELLOW);
        slideBody6.setStrokeWidth(3.0);

        Circle slideEnd6 = new Circle(225, 825, 20);
        slideEnd6.setFill(Color.YELLOW);
        slideEnd6.setStroke(Color.YELLOW);
        slideEnd6.setStrokeWidth(3.0);

        root.getChildren().add(slideBody6);
        root.getChildren().add(slideEnd6);
        root.getChildren().add(slideArrow6);

        Polygon slideArrow7 = new Polygon();

        slideArrow7.getPoints().addAll(105.0, 795.0, 145.0, 795.0, 125.0, 760.0);
        slideArrow7.setFill(Color.GREEN);
        slideArrow7.setStroke(Color.GREEN);
        slideArrow7.setStrokeWidth(3.0);

        Polygon slideBody7 = new Polygon();
        slideBody7.getPoints().addAll(120.0, 770.0, 120.0, 630.0, 130.0, 630.0, 130.0, 770.0);
        slideBody7.setFill(Color.GREEN);
        slideBody7.setStroke(Color.GREEN);
        slideBody7.setStrokeWidth(3.0);

        Circle slideEnd7 = new Circle(125, 625, 20);
        slideEnd7.setFill(Color.GREEN);
        slideEnd7.setStroke(Color.GREEN);
        slideEnd7.setStrokeWidth(3.0);

        root.getChildren().add(slideBody7);
        root.getChildren().add(slideEnd7);
        root.getChildren().add(slideArrow7);

        Polygon slideArrow8 = new Polygon();
      
        slideArrow8.getPoints().addAll(105.0, 395.0, 145.0, 395.0, 125.0, 360.0);
        slideArrow8.setFill(Color.GREEN);
        slideArrow8.setStroke(Color.GREEN);
        slideArrow8.setStrokeWidth(3.0);

        Polygon slideBody8 = new Polygon();
        slideBody8.getPoints().addAll(120.0, 370.0, 120.0, 180.0, 130.0, 180.0, 130.0, 370.0);
        slideBody8.setFill(Color.GREEN);
        slideBody8.setStroke(Color.GREEN);
        slideBody8.setStrokeWidth(3.0);

        Circle slideEnd8 = new Circle(125, 175, 20);
        slideEnd8.setFill(Color.GREEN);
        slideEnd8.setStroke(Color.GREEN);
        slideEnd8.setStrokeWidth(3.0);

        root.getChildren().add(slideBody8);
        root.getChildren().add(slideEnd8);
        root.getChildren().add(slideArrow8);

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

        /*sideBar.getChildren().remove(cardDescription);
        sideBar.getChildren().remove(cardNumber);
        sideBar.getChildren().remove(remainingCards);
*/

        //Label label1;
        if (card.getNumber() == 0) {

            cardNumber = new Label("Card: Sorry!");
            cardNumber.setTranslateY(100);
            cardNumber.setTranslateX(1180);
            cardNumber.setFont(new Font("Times New Roman", 30));
            root.getChildren().add(cardNumber);
        } else {
            cardNumber = new Label("Card: " + card.getNumber());
            cardNumber.setTranslateY(100);
            cardNumber.setTranslateX(1180);
            cardNumber.setFont(new Font("Times New Roman", 30));
            root.getChildren().add(cardNumber);
        }

        cardDescription = new Label("Description: " + card.getDescription());
        cardDescription.setTranslateY(150);
        cardDescription.setTranslateX(1050);
        cardDescription.setMaxWidth(375);
        cardDescription.setFont(new Font("Times New Roman", 20));

        remainingCards = new Label();
        remainingCards.setTranslateY(835);
        remainingCards.setTranslateX(1050);
        remainingCards.setMaxWidth(375);

        remainingCards.textProperty().bind(Bindings.concat("Cards left: ").concat(new SimpleIntegerProperty(deck.cardsRemaining()).asString()));

        //remainingCards.textProperty().bind(Bindings.concat("Cards left: ").concat(new SimpleIntegerProperty(deck.cardsRemaining()).asString()));


        sideBar.getChildren().add(bar);
        sideBar.getChildren().add(cardNumber);
        sideBar.getChildren().add(cardDescription);
        sideBar.getChildren().add(remainingCards);

        Button endGame = new Button("Exit Game");
        endGame.setTranslateX(1350);
        endGame.setTranslateY(835);
        sideBar.getChildren().add(endGame);
        endGame.setOnMouseClicked(event ->Platform.exit());

        root.getChildren().add(sideBar);
        
    }


    public static void main(String[] args) {
        Application.launch(args);
    }


}
