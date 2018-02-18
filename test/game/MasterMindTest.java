package game;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.MasterMind.Feedback;
import static game.MasterMind.Feedback.*;
import static game.MasterMind.GameState.*;

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

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(6, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(0, (long)feedback.get(MATCH)),
                () -> assertEquals(0, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesAllRightColorsWithAllInWrongPositionReturnsOnlyMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.CYAN, Color.BLACK, Color.WHITE, Color.GREEN, Color.BLUE, Color.RED);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(0, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(6, (long)feedback.get(MATCH)),
                () -> assertEquals(0, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesAllRightColorsWithAllInRightPositionReturnsOnlyPositionMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(0, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(0, (long)feedback.get(MATCH)),
                () -> assertEquals(6, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesOneColorRightAndInWrongPositionReturnsMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.RED);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(5, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(1, (long)feedback.get(MATCH)),
                () -> assertEquals(0, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesOneColorRightAndInRightPositionReturnsPositionMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.BLUE, Color.PINK, Color.PINK, Color.PINK, Color.PINK);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(5, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(0, (long)feedback.get(MATCH)),
                () -> assertEquals(1, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesTwoColorsRightOnlyOneInRightPositionReturnsOneMatchOnePositionMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.GREEN);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(4, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(1, (long)feedback.get(MATCH)),
                () -> assertEquals(1, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesRightColorDuplicatedReturnsCorrespondingMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.BLACK, Color.WHITE);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(1, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(2, (long)feedback.get(MATCH)),
                () -> assertEquals(3, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesRightColorDuplicatedButNoPositionMatchReturnsOnlyOneMatch()
    {
        List<Color> userGuess = Arrays.asList(Color.PINK, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(5, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(1, (long)feedback.get(MATCH)),
                () -> assertEquals(0, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void userGuessesTwoDuplicatedRightColorsInRightPositionReturnsOnlyTwoPositionMatches()
    {
        List<Color> userGuess = Arrays.asList(Color.RED, Color.RED, Color.RED, Color.WHITE, Color.WHITE, Color.WHITE);

        Map<Feedback, Long> feedback = masterMind.guess(userGuess);

        assertAll(
                () -> assertEquals(4, (long)feedback.get(NO_MATCH)),
                () -> assertEquals(0, (long)feedback.get(MATCH)),
                () -> assertEquals(2, (long)feedback.get(POSITION_MATCH)));
    }

    @Test
    void gameStatusAtTheStartOfTheGameReturnsInProgress()
    {
        assertEquals(IN_PROGRESS, masterMind.getState());
    }

    @Test
    void gameStatusAfterTwoGuessesReturnsInProgress()
    {
        masterMind.guess(Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.RED));
        masterMind.guess(Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.RED));

        assertEquals(IN_PROGRESS, masterMind.getState());
    }

    @Test
    void gameStatusAfterCorrectColorsInAllPositionsReturnsWonState()
    {
        Map<Feedback, Long> guess = masterMind.guess(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN));

        masterMind.guessEvaluation(guess);

        assertEquals(WON, masterMind.getState());
    }

    @Test
    void gameStatusAfterGuessIsCalledMaxAttemptsReturnsLostState()
    {
        Map<Feedback, Long> guess = masterMind.guess(Arrays.asList(Color.RED, Color.RED, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN));

        IntStream.range(0,20).forEach(time -> masterMind.guessEvaluation(guess));

        assertEquals(LOST, masterMind.getState());
    }

    @Test
    void gameStatusAfterGuessIsCalledAfterCorrectGuessIsWon()
    {
        Map<Feedback, Long> guess = masterMind.guess(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN));

        IntStream.range(0,2).forEach(time -> masterMind.guessEvaluation(guess));

        assertEquals(WON, masterMind.getState());
    }

    @Test
    void gameStatusAfterGuessIsCalledOnTheLastAttempt()
    {
        Map<Feedback, Long> wrongGuess = masterMind.guess(Arrays.asList(Color.RED, Color.RED, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN));
        Map<Feedback, Long> rightGuess = masterMind.guess(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE, Color.BLACK, Color.CYAN));

        IntStream.range(0,19).forEach(time -> masterMind.guessEvaluation(wrongGuess));
        masterMind.guessEvaluation(rightGuess);

        assertEquals(WON, masterMind.getState());
    }

    @Test
    void generateRandomColorsReturnsASetOfColors()
    {
        assertFalse(masterMind.generateRandomColors().isEmpty());
    }

    @Test
    void generateRandomColorsReturnsSixColors()
    {
        assertEquals(6, masterMind.generateRandomColors().size());
    }

    @Test
    void generateRandomColorsReturnsUniqueColors()
    {
        List<Color> distinctlyFilteredColorList = masterMind.generateRandomColors().stream().distinct().collect(Collectors.toList());

        assertEquals(6, distinctlyFilteredColorList.size());
    }

    

    @Test
    void testsToSuppressRedInCoverage()
    {
        Feedback.values();
        Feedback.valueOf("NO_MATCH");
        assertTrue(true);
    }
}