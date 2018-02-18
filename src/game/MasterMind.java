package game;

import java.awt.*;
import java.util.*;
import java.util.List;

import java.util.stream.IntStream;
import java.util.function.IntFunction;
import java.util.function.Function;

import static java.awt.Color.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;

import static game.MasterMind.Feedback.*;
import static game.MasterMind.GameState.*;

public class MasterMind
{
    public enum Feedback { NO_MATCH, MATCH, POSITION_MATCH }

    public enum GameState { IN_PROGRESS, LOST, WON}

    private GameState gameState;

    private int userAttempts;

    private List<Color> gameColors = new ArrayList<>(Arrays.asList(GREEN, BLUE, YELLOW, RED, PINK, ORANGE, MAGENTA, GRAY, DARK_GRAY, LIGHT_GRAY));

    private final int SIZE = 6;
    private final int MAX_ATTEMPTS = 20;


    private List<Color> selection;

    protected MasterMind(List<Color> listOfColors)
    {
        selection = listOfColors;
        gameState = IN_PROGRESS;
        userAttempts = 0;
    }

    public MasterMind()
    {
        selection = generateRandomColors();
        gameState = IN_PROGRESS;
        userAttempts = 0;
    }

    protected List<Color> generateRandomColors()
    {
         Collections.shuffle(gameColors);

         return gameColors.subList(0, SIZE);
    }

    public Map<Feedback, Long> guess(List<Color> userGuess)
    {
        IntFunction<Feedback> computeMatchAtPosition = index ->
          selection.get(index) == userGuess.get(index) ? POSITION_MATCH :
            userGuess.contains(selection.get(index)) ? MATCH : NO_MATCH;

         Map<Feedback, Long> feedback =
           IntStream.range(0, SIZE)
             .mapToObj(computeMatchAtPosition)
             .collect((groupingBy(Function.identity(), counting())));

         feedback.computeIfAbsent(NO_MATCH, key -> 0L);
         feedback.computeIfAbsent(MATCH, key -> 0L);
         feedback.computeIfAbsent(POSITION_MATCH, key -> 0L);

        return feedback;
    }

    public void guessEvaluation(Map<Feedback, Long> guess)
    {

        if (guess.get(POSITION_MATCH) == SIZE && userAttempts <= MAX_ATTEMPTS)
            gameState = WON;
        else if (userAttempts < MAX_ATTEMPTS - 1)
            userAttempts = userAttempts + 1;
        else
            gameState = LOST;
    }

    public GameState getState()
    {
        return gameState;
    }
}