package game;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MasterMind
{
    public enum Feedback {NO_MATCH, MATCH, POSITION_MATCH}

    private List<Color> solutionList;

    protected MasterMind(List<Color> listOfColors)
    {
        solutionList = listOfColors;
    }

    public Map<Feedback, Integer> guess(List<Color> userGuessList)
    {
        Map<Feedback, Integer> feedbackList = new HashMap<>();

        for (int solutionListIndex = 0; solutionListIndex < 6; solutionListIndex++)
        {
            for (int guessListIndex = 0; guessListIndex < 6; guessListIndex++)
            {
                if (solutionList.get(solutionListIndex) == userGuessList.get(guessListIndex) && solutionListIndex == guessListIndex)
                {
                    feedbackList.put(Feedback.POSITION_MATCH, feedbackList.getOrDefault(Feedback.POSITION_MATCH, 0) + 1);
                    break;
                }

                else if (solutionList.get(solutionListIndex) == userGuessList.get(guessListIndex))
                {
                    feedbackList.put(Feedback.MATCH, feedbackList.getOrDefault(Feedback.MATCH, 0) + 1);
                    break;
                }
            }
        }

        feedbackList.put(Feedback.NO_MATCH, 6 - (feedbackList.getOrDefault(Feedback.POSITION_MATCH, 0) + feedbackList.getOrDefault(Feedback.MATCH, 0)));

        return feedbackList;
    }

}