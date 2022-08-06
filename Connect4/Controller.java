package com.myGame.Connect4;


import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertDiscPane;
    @FXML
    public Label playerOne;
    @FXML
    public Label turn;
    @FXML
    public TextField playerOneTextField,playerTwoTextField;
    @FXML
    public Button setNamesButton;


    private static final int COL=7;
    private static final int ROW=6;
    private static final int CIR_DIAMETER=70;
    private static final String discColor1="#24303E";
    private static final String disColor2="#4CAA88";
    private static  String player_1="Player One";
    private static  String player_2="Player Two";
    private boolean isPlayerOneTurn=true;
    private Disc[][] insertedDiscsArray=new Disc[ROW][COL];
    private boolean isAllowedToInsert=true;
    public void createPlayground()
    {
        Shape rectangleWithHoles=createGameStructuralGrid();
        setNamesButton.setOnAction(actionEvent -> {
            player_1=playerOneTextField.getText();
            player_2=playerTwoTextField.getText();
        });
        rootGridPane.add(rectangleWithHoles,0,1);

        List<Rectangle>rectangle=createClickableColumn();
        for (Rectangle rect:rectangle
             ) {
            rootGridPane.add(rect,0,1);
        }

    }
    public Shape createGameStructuralGrid()
    {
        Shape rectangleWithHoles= new Rectangle((COL+1)*CIR_DIAMETER,(ROW+1)*CIR_DIAMETER);
        for (int row=0;row<ROW;row++)
        {
            for (int col=0;col<COL;col++)
            {
                Circle circle=new Circle();
                circle.setRadius(CIR_DIAMETER/2);
                circle.setCenterX(CIR_DIAMETER/2);
                circle.setCenterY(CIR_DIAMETER/2);
                circle.setSmooth(true);
                circle.setTranslateX(col*(CIR_DIAMETER+5)+CIR_DIAMETER/4);
                circle.setTranslateY(row*(CIR_DIAMETER+5)+CIR_DIAMETER/4);
                rectangleWithHoles=Shape.subtract(rectangleWithHoles,circle);
            }
        }
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }
    private List<Rectangle> createClickableColumn()
    {
        List<Rectangle>rectangleList=new ArrayList<Rectangle>();
        for( int col=0;col<COL;col++) {
            final Rectangle rectangle = new Rectangle(CIR_DIAMETER, (ROW + 1) * CIR_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CIR_DIAMETER+5)+CIR_DIAMETER/4);
            rectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rectangle.setFill(Color.valueOf("#eeeeee26"));
                }
            });
            rectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rectangle.setFill(Color.TRANSPARENT);
                }
            });
            final int column=col;
            rectangle.setOnMouseClicked(mouseEvent -> {if(isAllowedToInsert){isAllowedToInsert=false;insertDisc(new Disc(isPlayerOneTurn),column);}});
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }
    private void insertDisc(Disc disc, final int column)
    {
        int row =ROW-1;
        while(row>=0)
        {
            if(getDiscPresent(row,column)==null)
                break;
            row--;
        }
        if(row<0)
            return;




        insertedDiscsArray[row][column]=disc;
        insertDiscPane.getChildren().add(disc);
        disc.setTranslateX(column*(CIR_DIAMETER+5)+CIR_DIAMETER/4);
        TranslateTransition translateTransition= new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(CIR_DIAMETER+5)+CIR_DIAMETER/4);
        final int currentRow=row;
        translateTransition.setOnFinished(actionEvent -> { isAllowedToInsert=true;
            if (gameEnded(currentRow,column))
            {
                gameOver();
                return;
            }
            isPlayerOneTurn=!isPlayerOneTurn;
            playerOne.setText(isPlayerOneTurn?player_1:player_2);
        });
        translateTransition.play();
    }
    private boolean gameEnded(int row , int column)
    {
        List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3).mapToObj(r->new Point2D(r,column)).collect(Collectors.toList());
        List<Point2D> horizontalPoints= IntStream.rangeClosed(column-3,column+3).mapToObj(col->new Point2D(row,col)).collect(Collectors.toList());
        Point2D startPoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonalPoint1=IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());
        Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonalPoint2=IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());
        boolean isEnded = checkCombination(verticalPoints)||checkCombination(horizontalPoints)||checkCombination(diagonalPoint1)||checkCombination(diagonalPoint2);
        return isEnded;
    }
    private  boolean checkCombination(List<Point2D> points)
    {
        int chain=0;
        for (Point2D point:points
             ) {

            int rowIndexForArray=(int)point.getX();
            int columnIndexForArray=(int)point.getY();
            Disc disc=getDiscPresent(rowIndexForArray,columnIndexForArray);
            if(disc!=null && disc.isPlayerOneMove==isPlayerOneTurn)
            {
                chain++;
                if(chain==4)
                {
                 return true;
                }
            }
            else
            {
                chain=0;
            }
        }
        return false;
    }
    private Disc getDiscPresent(int row, int column)
    {
        if(row>=ROW||row<0||column>=COL||column<0)
            return null;
        return insertedDiscsArray[row][column];
    }
    private void gameOver()
    {
        String winner= isPlayerOneTurn?player_1:player_2;
        System.out.println(winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to play again ?");
        ButtonType yesButton =new ButtonType("Yes");
        ButtonType noButton =new ButtonType("No,EXit");
        alert.getButtonTypes().setAll(yesButton,noButton);

        Platform.runLater(()->{
            Optional<ButtonType> btnClicked=alert.showAndWait();
            if(btnClicked.isPresent()&&btnClicked.get()==yesButton)
            {
                resetGame();
            }
            else
            {
                Platform.exit();
                System.exit(0);
            }
        });


    }
    public void resetGame()
    {
        insertDiscPane.getChildren().clear();
        for (int row = 0; row <insertedDiscsArray.length ; row++) {
            for (int col = 0; col < insertedDiscsArray[row].length; col++) {
                insertedDiscsArray[row][col]=null;
            }
        }
        isPlayerOneTurn=true;
        playerOne.setText(player_1);
        createPlayground();
    }
    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;
        public Disc(boolean isPlayerOneMove)
        {
            this.isPlayerOneMove=isPlayerOneMove;
            setRadius(CIR_DIAMETER/2);
            setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(disColor2));
            setCenterX(CIR_DIAMETER/2);
            setCenterY(CIR_DIAMETER/2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}