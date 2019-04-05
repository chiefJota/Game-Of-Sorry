import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // stage title
        primaryStage.setTitle("Sorry!");

        // root group
        Group root = new Group();

        // PlayerBoard object, don't worry about this for now
        PlayerBoard board = new PlayerBoard();

        makeBoard(root, board);

        //create new Sorry! game deck
        SorryDeck deck = new SorryDeck();

        //shuffle the deck
        deck.shuffle();

        makeSidebar(root, deck.getTopCard(), deck.cardsRemaining());

        //Part of this function was taken from https://www.tutorialspoint.com/javafx/javafx_event_handling.htm
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            int count = 0;
            int x, y;
            int subtract;

            public void handle(MouseEvent e) {
                //Calculates the coordinates of your click
                x = (int)e.getX();
                y = (int)e.getY();
                subtract = x%25;
                x = x - subtract;
                subtract = y%25;
                y = y - subtract;

                //Need to write a function that returns what tile you're clicking on
                System.out.println(x);
                System.out.println(y);

                //Moves the pawn and remakes the board
                board.movePawn(board.getTileID(x, y), deck.getTopCard().getNumber());
                //print out every card
                System.out.println(deck.getTopCard().getNumber());
                //count++;
                makeBoard(root, board);


            }
        };


        root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

        // this removes the pawns
        //root.getChildren().remove(boardDisplay);

        // this displays the scene with the resolution.
        primaryStage.setScene(new Scene(root, 1600, 900));

        primaryStage.show();
    }

    public void makeBoard(Group root, PlayerBoard board) {
        // Code to create the board display, don't be afraid to put this in a function or something i'm just lazy
        Circle start = new Circle(325, 150, 50, Color.WHITE);
        start.setStroke(Color.BLACK);

        Circle home = new Circle(225, 400, 50, Color.WHITE);
        home.setStroke(Color.BLACK);

        root.getChildren().add(start);
        root.getChildren().add(home);

        for (int i = 0; i < 5; i++) {
            Rectangle square = new Rectangle(200, 50 * i + 100, 50, 50);

            square.setFill(Color.WHITE);
            square.setStroke(Color.BLACK);

            root.getChildren().add(square);
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

    private void makeSidebar(Group root, SorryCard card, int remainingCards) {
        Rectangle bar = new Rectangle(1000, 0, 25, 900);
        bar.setFill(Color.BLACK);
        Label label1, label2, label3;
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

        label2 = new Label("Description: " + card.getDescription());
        label2.setTranslateY(150);
        label2.setTranslateX(1050);
        label2.setMaxWidth(375);
        label2.setFont(new Font("Times New Roman", 20));

        label3 = new Label("Cards remaining in deck: " + remainingCards);
        label3.setTranslateY(835);
        label3.setTranslateX(1050);
        label3.setMaxWidth(375);

        root.getChildren().add(bar);
        root.getChildren().add(label2);
        root.getChildren().add(label3);
    }



    public static void main(String[] args) {
        Application.launch(args);
    }


}
