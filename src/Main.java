import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import javafx.animation.AnimationTimer;

import javax.sound.midi.SysexMessage;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class Main extends Application {

    private SorryDeck deck;
    private SorryCard card;
    private int turn = 0;
    private int x = 0;
    private int y = 0;
    private int choice = -1;
    private int click2 = 0;
    private int pawnIDs = -1;
    private int pawnID2 = -1;
    private int frame = 0;

    private Button move1;
    private Button move2;
    private Button move3;
    private Button move4;
    private Button move5;
    private Button move6;
    private Button move7;

    private Button forfeitTurn;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // stage title
        primaryStage.setTitle("Sorry!");

        //create an instance of the game
        deck = new SorryDeck();

        deck.shuffle();

        // root group
        root = new BorderPane();

        Group menu = new Group();
        Scene startMenu = new Scene(menu, 1450, 900);

        Group sorryRules = new Group();
        Scene rulesScene = new Scene(sorryRules, 1450, 900);

        makeMenu(menu, startMenu, sorryRules, rulesScene, root, primaryStage);

        PlayerBoard[] boards = new PlayerBoard[2];
        boards[0] = new PlayerBoard(0, Color.RED);
        boards[1] = new PlayerBoard(1, Color.BLUE);
        //PlayerBoard board3 = new PlayerBoard(3, Color.GREEN);

        ComputerPlayer cpu1 = new ComputerPlayer(1);

        makeBoard(root);

        for (PlayerBoard board : boards) {
            Group pawns = board.displayPawns();
            root.getChildren().add(pawns);
        }

        // this displays the scene with the resolution.
        primaryStage.setScene(startMenu);
        primaryStage.show();

        //Choose first player to go
        Random rand = new Random();
        //turn = rand.nextInt(4);
        turn = 0;

        EventHandler<MouseEvent> getcoords = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //Calculates the coordinates of your click
                if (e.getX() < 1000) {
                    x = (int) e.getX();
                    y = (int) e.getY();
                }
            }
        };

       /* EventHandler<MouseEvent> getcoords2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //Calculates the coordinates of your click
                if (e.getX() < 1000) {
                   // x2 = (int) e.getX();
                   // y2 = (int) e.getY();
                }
                System.out.println("click2");
            }
        };*/

        new AnimationTimer() {
            public void handle(long currentNanoTime) {

                if (turn % 2 == 1) {
                    if (frame % 10 == 0) {
                        boolean didturn = cpu1.doTurn(boards, card.getNumber(), turn);

                        if (didturn) {
                            if (didturn && !(card.getNumber() == 2)) {turn++;}
                            makeBoard(root);

                            for (PlayerBoard board : boards) {
                                Group pawns = board.displayPawns();
                                root.getChildren().add(pawns);
                            }

                            //TODO: Put timer to delay sidebar update
                            makeSidebar(root, changeCard());
                        } else {
                            System.out.println("turn skipped");
                            System.out.println(card.getNumber());
                            if (!(card.getNumber() == 2)) {turn++;}
                            makeBoard(root);

                            for (PlayerBoard board : boards) {
                                Group pawns = board.displayPawns();
                                root.getChildren().add(pawns);
                            }

                            //TODO: Put timer to delay sidebar update
                            makeSidebar(root, changeCard());
                        }
                    } else {
                        makeBoard(root);

                        for (PlayerBoard board : boards) {
                            Group pawns = board.displayPawns();
                            root.getChildren().add(pawns);
                        }

                        //TODO: Put timer to delay sidebar update
                        makeSidebar(root, card);
                    }
                } else
                    try {

                    switch (card.getNumber()) {
                        case 0:
                            moveSorry(boards, root, getcoords);
                            break;
                        case 1:
                            move1(boards, root, getcoords);
                            break;
                        case 2:
                            move2(boards, root, getcoords);
                            break;
                        case 4:
                            move4(boards, root, getcoords);
                            break;
                        case 7:
                            moveSeven(boards, root, getcoords);
                            break;
                        case 10:
                            moveTen(boards, root, getcoords);
                            break;
                        case 11:
                            moveEleven(boards, root, getcoords);
                            break;
                        default:
                            PlayerBoard activeBoard = boards[turn % 2];

                            root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                           /* if(deck.isEmpty()){
                                forfeitTurn.setDisable(true);
                                deck = new SorryDeck();
                                deck.shuffle();
                            }*/

                            if (activeBoard.canMovePawn(activeBoard.getTileID(x, y), card.getNumber())) {
                                //Moves the pawn and remakes the board
                                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), card.getNumber());
                                for (PlayerBoard board : boards) {
                                    if (!(board.getRotation() == turn % 2)) {
                                        board.bump(shortBump, turn % 2);
                                    }
                                }

                                int[] longBump = activeBoard.checkSlide();

                                for (PlayerBoard board : boards) {
                                    if (!(board.getRotation() == turn % 2)) {
                                        board.bump(longBump, turn % 2);
                                    }
                                }

                                activeBoard.moveToHome();

                                makeBoard(root);

                                for (PlayerBoard board : boards) {
                                    Group pawns = board.displayPawns();
                                    root.getChildren().add(pawns);
                                }
                                ++turn;


                               // card = deck.getTopCard();

                                //TODO: Put timer to delay sidebar update
                                makeSidebar(root, changeCard());

                                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);
                            }
                            break;
                    }

                } catch (Exception exception) {
                    //System.out.println("You did not click on a board tile.");
                }

                frame++;

            }
        }.start();


       // card = deck.getTopCard();

        makeSidebar(root, changeCard());



            /* Possibly use this to highlight where your pawns can move
            root.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {

                    }
                    );
                  */


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

        Image background = new Image("/SorryGameMenuImage.jpg", true);
        ImageView back1 = new ImageView(background);
        back1.setFitHeight(500);
        back1.setFitWidth(800);
        back1.setX(325);
        back1.setY(100);
        menu.getChildren().add(back1);
    }

    private void makeBoard(BorderPane root) {

        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        //Place SorryCenterGameImage in the center of the board
        Image centerImage = new Image("/SorryCenterGameImage.png", true);
        ImageView center = new ImageView(centerImage);
        center.setFitHeight(264);
        center.setFitWidth(408);
        center.setX(300);
        center.setY(300);
        root.getChildren().add(center);

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
        slideArrow5.setStrokeWidth(3.0);

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
        slideArrow6.setStrokeWidth(3.0);

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

    }

    private void makeSidebar(BorderPane root, SorryCard card) {
        Rectangle bar = new Rectangle(1000, 0, 25, 900);
        Rectangle bar2 = new Rectangle(1000, 0, 4, 900);
        Rectangle bar3 = new Rectangle(1025, 0, 4, 900);
        bar2.setFill(Color.BLACK);
        bar3.setFill(Color.BLACK);

        if (turn % 2 == 0) {
            bar.setFill(Color.RED);
        } else if (turn % 2 == 1) {
            bar.setFill(Color.BLUE);
        } else if (turn % 2 == 2) {
            bar.setFill(Color.YELLOW);
        } else if (turn % 2 == 3) {
            bar.setFill(Color.GREEN);
        }


        Rectangle white = new Rectangle(1025, 0, 425, 900);
        white.setFill(Color.WHITE);

        Group sideBar = new Group();
        Label remainingCards;
        Label cardDescription;
        Label cardNumber;


        if (card.getNumber() == 0) {
            cardNumber = new Label("Card: Sorry!");
            cardNumber.setTranslateY(100);
            cardNumber.setTranslateX(1180);
            cardNumber.setFont(new Font("Times New Roman", 30));
        } else {
            cardNumber = new Label("Card: " + card.getNumber());
            cardNumber.setTranslateY(100);
            cardNumber.setTranslateX(1180);
            cardNumber.setFont(new Font("Times New Roman", 30));
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


        sideBar.getChildren().add(white);
        sideBar.getChildren().add(bar);
        sideBar.getChildren().add(bar2);
        sideBar.getChildren().add(bar3);
        sideBar.getChildren().add(cardNumber);
        sideBar.getChildren().add(cardDescription);
        sideBar.getChildren().add(remainingCards);


        switch (card.getNumber()) {
            case 1:
                Button button1 = new Button("Move pawn from Start");
                button1.setTranslateX(1150);
                button1.setTranslateY(400);
                sideBar.getChildren().add(button1);
                button1.setOnMouseClicked(event -> {
                    choice = 0;
                });
                Button button2 = new Button("Move forward 1");
                button2.setTranslateX(1160);
                button2.setTranslateY(450);
                sideBar.getChildren().add(button2);
                button2.setOnMouseClicked(event -> {
                    choice = 1;
                });
                break;
            case 2:
                Button button3 = new Button("Move pawn from Start");
                button3.setTranslateX(1150);
                button3.setTranslateY(400);
                sideBar.getChildren().add(button3);
                button3.setOnMouseClicked(event -> {
                    choice = 0;
                });
                Button button4 = new Button("Move forward 2");
                button4.setTranslateX(1160);
                button4.setTranslateY(450);
                sideBar.getChildren().add(button4);
                button4.setOnMouseClicked(event -> {
                    choice = 1;
                });
                break;
            case 7:
                move1 = new Button("Move 1");
                move1.setTranslateX(1195);
                move1.setTranslateY(400);
                sideBar.getChildren().add(move1);
                move1.setOnMouseClicked(event -> {
                    choice = 0;
                });
                move2 = new Button("Move 2");
                move2.setTranslateX(1195);
                move2.setTranslateY(450);
                sideBar.getChildren().add(move2);
                move2.setOnMouseClicked(event -> {
                    choice = 1;
                });
                move3 = new Button("Move 3");
                move3.setTranslateX(1195);
                move3.setTranslateY(500);
                sideBar.getChildren().add(move3);
                move3.setOnMouseClicked(event -> {
                    choice = 2;
                });
                move4 = new Button("Move 4");
                move4.setTranslateX(1195);
                move4.setTranslateY(550);
                sideBar.getChildren().add(move4);
                move4.setOnMouseClicked(event -> {
                    choice = 3;
                });
                move5 = new Button("Move 5");
                move5.setTranslateX(1195);
                move5.setTranslateY(600);
                sideBar.getChildren().add(move5);
                move5.setOnMouseClicked(event -> {
                    choice = 4;
                });
                move6 = new Button("Move 6");
                move6.setTranslateX(1195);
                move6.setTranslateY(650);
                sideBar.getChildren().add(move6);
                move6.setOnMouseClicked(event -> {
                    choice = 5;
                });
                move7 = new Button("Move 7");
                move7.setTranslateX(1195);
                move7.setTranslateY(700);
                sideBar.getChildren().add(move7);
                move7.setOnMouseClicked(event -> {
                    choice = 6;
                });
                break;
            case 10:
                Button button5 = new Button("Move forward 10");
                button5.setTranslateX(1150);
                button5.setTranslateY(350);
                sideBar.getChildren().add(button5);
                button5.setOnMouseClicked(event -> {
                    choice = 0;
                });
                Button button6 = new Button("Move back 1");
                button6.setTranslateX(1160);
                button6.setTranslateY(400);
                sideBar.getChildren().add(button6);
                button6.setOnMouseClicked(event -> {
                    choice = 1;
                });
                break;
            case 11:
                Button button7 = new Button("Move forward 11");
                button7.setTranslateX(1150);
                button7.setTranslateY(400);
                sideBar.getChildren().add(button7);
                button7.setOnMouseClicked(event -> {
                    choice = 0;
                });
                Button button8 = new Button("Swap");
                button8.setTranslateX(1190);
                button8.setTranslateY(450);
                sideBar.getChildren().add(button8);
                button8.setOnMouseClicked(event -> {
                    choice = 1;
                });
                break;
        }


        forfeitTurn = new Button("Forfeit turn");
        forfeitTurn.setTranslateX(1250);
        forfeitTurn.setTranslateY(835);
        sideBar.getChildren().add(forfeitTurn);
        forfeitTurn.setDisable(false);

        //TODO: might have to check if the player can move
        // in here to enable and disable the forfeit turn
        // button
        forfeitTurn.setOnMouseClicked(event -> {
            //increment turn to
            //be opposing players
            ++turn;
            //update the sidebar
            makeSidebar(root, changeCard());

        });

        Button endGame = new Button("Exit Game");
        endGame.setTranslateX(1350);
        endGame.setTranslateY(835);
        sideBar.getChildren().add(endGame);
        endGame.setOnMouseClicked(event -> Platform.exit());


        root.getChildren().add(sideBar);
    }

    public void move1(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        int pawnID = activeBoard.getTileID(x, y);

        if ((choice == 0) && (!activeBoard.hasPawnAt(1))) {
            activeBoard.moveFromStart();

            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }

           /* if (deck.isEmpty()) {
                deck = new SorryDeck();
                deck.shuffle();
            }*/

        //    card = deck.getTopCard();

            turn++;
            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());


            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

            reset();
        } else if ((choice == 1) && (activeBoard.hasPawnAt(pawnID))) {
            if (activeBoard.canMovePawn(pawnID, 1)) {

                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), 1);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }

                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }
/*
                if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }*/

              //  card = deck.getTopCard();

                ++turn;
                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());


                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                reset();
            }
        }

    }


    public void move2(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        int pawnID = activeBoard.getTileID(x, y);

        if ((choice == 0) && (!activeBoard.hasPawnAt(1))) {
            activeBoard.moveFromStart();

            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }

          /*  if (deck.isEmpty()) {
                deck = new SorryDeck();
                deck.shuffle();
            }*/
          //  card = deck.getTopCard();

            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());

            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

            reset();
        } else if ((choice == 1) && (activeBoard.hasPawnAt(pawnID))) {
            if (activeBoard.canMovePawn(pawnID, 2)) {

                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), 2);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }

                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }


               /* if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }*/

             //   card = deck.getTopCard();

                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());

                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                reset();
            }
        }

    }


    public void move4(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {
        PlayerBoard activeBoard = boards[turn % 2];

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        if (activeBoard.canMovePawn(activeBoard.getTileID(x, y), -4)) {

            int bumped1 = activeBoard.movePawn(activeBoard.getTileID(x, y), -4);
            int[] bumped11 = new int[]{bumped1};
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(bumped11, turn % 2);
                }
            }

            int[] bumped = activeBoard.checkSlide();

            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(bumped, turn % 2);
                }
            }
            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }

            ++turn;


            if (deck.cardsRemaining() == 0) {
                deck.shuffle();
            }

            //card = deck.getTopCard();

            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());

            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);
            reset();

        }
    }

    void moveSeven(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        if (click2 == 0 && (activeBoard.hasPawnAt(activeBoard.getTileID(x, y), turn % 2)) && (activeBoard.getTileID(x, y) != -1)) {
            pawnIDs = activeBoard.getTileID(x, y);
        }

        if (pawnIDs != -1) {
            ++click2;
        }
        if ((click2 != 0) && (activeBoard.hasPawnAt(activeBoard.getTileID(x, y), turn % 2))) {
            pawnID2 = activeBoard.getTileID(x, y);
        }

        //System.out.println(pawnIDs);
        //System.out.println(pawnID2);

        int checkPawn1 = pawnIDs + (choice+1);
        int checkPawn2 = pawnID2 + (7-(choice+1));

        if (choice == 6 && activeBoard.canMovePawn(pawnIDs, 7)){

            int shortBump = activeBoard.movePawn(pawnIDs, 7);
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(shortBump, turn % 2);
                }
            }

            int[] longBump = activeBoard.checkSlide();

            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(longBump, turn % 2);
                }
            }

            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }
            ++turn;


            //card = deck.getTopCard();

            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());

            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);
            reset();

        } else if (choice == 6 && activeBoard.canMovePawn(pawnID2, 7)){

            int shortBump = activeBoard.movePawn(pawnID2, 7);
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(shortBump, turn % 2);
                }
            }

            int[] longBump = activeBoard.checkSlide();

            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(longBump, turn % 2);
                }
            }

            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }
            ++turn;


            //card = deck.getTopCard();

            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());

            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);
            reset();


        } else if ((activeBoard.hasPawnAt(pawnIDs, turn % 2)) && activeBoard.hasPawnAt(pawnID2, turn % 2)) {
            if (activeBoard.canMovePawn(pawnIDs, choice + 1) && activeBoard.canMovePawn(pawnID2, 7 - (choice + 1)) && checkPawn1 != checkPawn2) {

                int shortBump = activeBoard.movePawn(pawnIDs, choice + 1);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }

                int shortBump1 = activeBoard.movePawn(pawnID2, 7 - (choice + 1));
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump1, turn % 2);
                    }
                }

                int[] longBump1 = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump1, turn % 2);
                    }
                }

                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }

                ++turn;

           /*     if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }
*/
               // card = deck.getTopCard();

                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());

                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);


                reset();
            }
        }

    }


    void moveTen(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        int pawnID = activeBoard.getTileID(x, y);

        if ((choice == 0) && (activeBoard.hasPawnAt(pawnID, turn % 2))) {
            if (activeBoard.canMovePawn(pawnID, 10)) {

                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), 10);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }

                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }

                ++turn;

             /*   if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }
*/
             //   card = deck.getTopCard();

                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());

                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                reset();
            }
        } else if ((choice == 1) && (activeBoard.hasPawnAt(pawnID))) {
            if (activeBoard.canMovePawn(pawnID, -1)) {

                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), -1);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }
                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }

                ++turn;

             /*   if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }*/

            //    card = deck.getTopCard();

                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());

                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                reset();
            }
        }
    }

    void moveEleven(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        if (choice == 0){


            if (activeBoard.canMovePawn(activeBoard.getTileID(x, y), 11)) {


                //Moves the pawn and remakes the board
                int shortBump = activeBoard.movePawn(activeBoard.getTileID(x, y), 11);
                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(shortBump, turn % 2);
                    }
                }

                int[] longBump = activeBoard.checkSlide();

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        board.bump(longBump, turn % 2);
                    }
                }

                activeBoard.moveToHome();

                makeBoard(root);

                for (PlayerBoard board : boards) {
                    Group pawns = board.displayPawns();
                    root.getChildren().add(pawns);
                }

                ++turn;

               /* if (deck.isEmpty()) {
                    deck = new SorryDeck();
                    deck.shuffle();
                }
*/
                //card = deck.getTopCard();

                //TODO: Put timer to delay sidebar update
                makeSidebar(root, changeCard());

                root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                reset();
            }


        } else if (choice == 1) {

            if (click2 == 0 && (activeBoard.hasPawnAt(activeBoard.getTileID(x, y), turn % 2)) && (activeBoard.getTileID(x, y)!=-1)){
                pawnIDs = activeBoard.getTileID(x, y);

            }
            if (pawnIDs != -1) {
                ++click2;
            }

            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    if (board.hasPawnAt(board.getTileID(x,y))){
                        pawnID2 = activeBoard.getTileID(x, y);
                    }
                }
            }

            boolean done1 = false;
            //System.out.println(pawnIDs);
            //System.out.println(pawnID2);

            if ((activeBoard.hasPawnAt(pawnIDs, turn % 2))) {

                for (PlayerBoard board : boards) {
                    if (!(board.getRotation() == turn % 2)) {
                        if (board.hasPawnAt(pawnID2, turn%2)) {
                            done1 = true;
                        }
                    }
                }

                if (done1) {

                    int spaces = pawnID2 - pawnIDs;

                    activeBoard.movePawn(pawnIDs, spaces);
                    //spaces = -(spaces);

                    for (PlayerBoard board : boards) {
                        if (!(board.getRotation() == turn % 2)) {
                            if (board.hasPawnAt(pawnID2, turn % 2)) {
                                board.movePawn(pawnID2, -(spaces), turn%2);
                            }
                        }
                    }

                    activeBoard.moveToHome();

                    makeBoard(root);

                    for (PlayerBoard board : boards) {
                        Group pawns = board.displayPawns();
                        root.getChildren().add(pawns);
                    }

                    ++turn;

                  /*  if (deck.isEmpty()) {
                        deck = new SorryDeck();
                        deck.shuffle();
                    }*/

                   // card = deck.getTopCard();

                    //TODO: Put timer to delay sidebar update
                    makeSidebar(root, changeCard());

                    root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

                    reset();
                }
            }
        }

    }

    void moveSorry(PlayerBoard[] boards, BorderPane root, EventHandler<MouseEvent> getcoords) {

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);

        PlayerBoard activeBoard = boards[turn % 2];

        int pawnID = activeBoard.getTileID(x, y);

        boolean done = false;

       canMove(boards, turn % 2, card.getNumber());

        //loop through other player board
        //check every tile to see
        //if there is a pawn on it
        for (PlayerBoard board : boards) {
            if (!(board.getRotation() == turn % 2)) {
                if (board.hasPawnAt(pawnID, turn % 2)) {
                    activeBoard.movePawnTo(pawnID);

                    done = true;
                }
            }
        }
        if (done) {
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(pawnID, turn % 2);
                }
            }

            int[] longBump = activeBoard.checkSlide();

            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == turn % 2)) {
                    board.bump(longBump, turn % 2);
                }
            }

            activeBoard.moveToHome();

            makeBoard(root);

            for (PlayerBoard board : boards) {
                Group pawns = board.displayPawns();
                root.getChildren().add(pawns);
            }

            //increment the turn
            turn++;

            /*if(deck.isEmpty()){
                deck = new SorryDeck();
                deck.shuffle();
            }*/

           // card = deck.getTopCard();

            //TODO: Put timer to delay sidebar update
            makeSidebar(root, changeCard());

            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, getcoords);
        }
        reset();
    }

    private boolean canMove(PlayerBoard[] boards, int rotation, int card){
        int[] myPawns = new int[60];
        int[] otherPawns = new int[60];
        int startPawns;
        int homePawns;

        Arrays.fill(myPawns, 0);
        Arrays.fill(otherPawns, 0);

        startPawns = boards[rotation].getStartPawns();
        homePawns = boards[rotation].getHomePawns();

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[rotation].hasPawnAt(i)) {
                myPawns[i] = 1;
            }
        }

        for (int i = 0; i < myPawns.length; i++) {
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == rotation)) {
                    if (board.hasPawnAt(i,rotation)){
                        otherPawns[i] = 1;
                    }
                }
            }
        }

        boolean canMovePawn = true;

        int pawnsOut = 4 - homePawns - startPawns;

        int lastPawnID = 0;

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[rotation].hasPawnAt(i)) {
                lastPawnID = i;
            }
        }

        // checks for the specific case where we have pawns lined up that can't move to home
        // and can't move foward
        if (lastPawnID > 0 && !(card == 10)) {
            if (card > 65 - lastPawnID) {
                if (pawnsOut == 1) {
                    if (myPawns[lastPawnID] == 1) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    }
                } else if (pawnsOut == 2) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    }
                } else if (pawnsOut == 3) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1 &&
                            myPawns[lastPawnID - 2 * card] == 1) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    }
                } else if (pawnsOut == 4) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1 &&
                            myPawns[lastPawnID - card] == 1 && myPawns[lastPawnID - card] == 1) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    }
                }
            }
        }

        if (!canMovePawn) {
            switch (card) {
                case 0:
                    if (homePawns > 0) {
                        canMovePawn = Arrays.asList(otherPawns).contains(1);
                    } else {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    }
                    break;
                case 1:
                    canMovePawn = true;
                case 2:
                    if (startPawns > 1 && myPawns[1] == 0){
                        canMovePawn = true;
                    }
                    break;
                case 7:
                    if (pawnsOut == 0) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    } else if (pawnsOut == 1 && lastPawnID + card > 65) {
                        canMovePawn = false;
                        forfeitTurn.setDisable(false);
                    } else {
                        canMovePawn = true;
                    }
                    break;
                case 11:
                    if (pawnsOut == 1 && lastPawnID + card > 65) {
                        if (Arrays.asList(otherPawns).contains(1)) {
                            canMovePawn = true;
                        } else {
                            canMovePawn = false;
                            forfeitTurn.setDisable(false);
                        }
                    }
                    break;
                default:
                    canMovePawn = Arrays.asList(myPawns).contains(1);
                    break;
            }
        }

        return canMovePawn;
    }


void reset(){
    choice = -1;
    click2 = 0;
    pawnIDs = -1;
    pawnID2 = -1;
    x = -1;
    y = -1;

}

    /**
     * Work around for lambda expression
     * Also, makes it so that a new
     * deck will be shuffled and game
     * will continue to play
     * @return
     */
    public SorryCard changeCard(){
        //draw the card
        card = deck.getTopCard();
        //check if the deck is empty or not
        if(deck.isEmpty()){
            deck = new SorryDeck();
            deck.shuffle();
        }
        return card;
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}



