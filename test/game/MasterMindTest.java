package game;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import game.MasterMind.Feedback;
import static game.MasterMind.Feedback.*;

public class MasterMindTest
{
    MasterMind masterMind;

    @BeforeEach
    void setUp()
    {
        List<Color> selection = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN);
        masterMind = new MasterMind(selection);
    }

    @Test
    void canary()
    {
        assertTrue(true);
    }

    @Test
    void userGuessesAllWrongColorsReturnsOnlyNoMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(NO_MATCH) == 6);
    }

    @Test
    void userGuessesAllRightColorsWithAllInWrongPositionReturnsOnlyMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.CYAN, Color.BLACK, Color.WHITE, Color.GREEN, Color.BLUE, Color.RED);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(MATCH) == 6);
    }

    @Test
    void userGuessesAllRightColorsWithAllInRightPositionReturnsOnlyPositionMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(POSITION_MATCH) == 6);
    }

    @Test
    void userGuessesOneColorRightAndInWrongPositionReturnsMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.RED);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(MATCH) == 1 && feedbackList.get(NO_MATCH) == 5);
    }

    @Test
    void userGuessesOneColorRightAndInRightPositionReturnsPositionMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.BLUE, Color.PINK, Color.PINK, Color.PINK, Color.PINK);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(POSITION_MATCH) == 1 && feedbackList.get(NO_MATCH) == 5);
    }

    @Test
    void userGuessesTwoColorsRightOnlyOneInRightPositionReturnsOneMatchOnePositionMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.GREEN);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(POSITION_MATCH) == 1 && feedbackList.get(MATCH) == 1 && feedbackList.get(NO_MATCH) == 4);
    }

    @Test
    void userGuessesRightColorDuplicatedReturnsCorrespondingMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.BLACK, Color.WHITE);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(POSITION_MATCH) == 3 && feedbackList.get(MATCH) == 2 && feedbackList.get(NO_MATCH) == 1);
    }

    @Test
    void userGuessesRightColorDuplicatedButNoPositionMatchReturnsOnlyOneMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(MATCH) == 1 && feedbackList.get(NO_MATCH) == 5);
    }

    @Test
    void userGuessesTwoDuplicatedRightColorsInRightPositionReturnsOnlyTwoPositionMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.RED, Color.RED, Color.WHITE, Color.WHITE, Color.WHITE);

        Map<Feedback, Integer> feedbackList = masterMind.guess(userGuess);

        assertTrue(feedbackList.get(POSITION_MATCH) == 2 && feedbackList.get(NO_MATCH) == 4);
    }

    @Test
    void testsToSuppressRedInCoverage()
    {
      Feedback.values();
      Feedback.valueOf("NO_MATCH");
      assertTrue(true);
    }
}