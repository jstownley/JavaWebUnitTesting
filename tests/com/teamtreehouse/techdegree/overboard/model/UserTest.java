package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by jonathanTownley on 7/14/16.
 */
public class UserTest {

    private Board board;
    private User alice;
    private User bob;
    private User charles;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        // Create a new board
        board = new Board("Java");

        // Create new users from the board
        alice = new User(board, "alice");
        bob = new User(board, "bob");
        charles = new User(board, "charles");

        // Users create questions
        question = alice.askQuestion("What is a String?");

        // Users can also answer questions
        answer = bob.answerQuestion(question, "It is a series of characters, strung together...");

    }

    @Test
    public void questionerReputationIncreasesBy5WhenQuestionUpvoted() throws Exception {
        charles.upVote(question);

        int reputation = alice.getReputation();

        assertEquals(5, reputation);
    }

    @Test
    public void answererReputationIncreasesBy10WhenAnswerUpvoted() throws Exception {
        int reputationBefore = bob.getReputation();
        charles.upVote(answer);

        int reputationAfter = bob.getReputation();

        assertEquals(10, reputationAfter - reputationBefore);
    }

    @Test
    public void answerReputationDecreasesBy1WhenAnswerDownvoted() throws Exception {
        int reputationBefore = bob.getReputation();
        charles.downVote(answer);

        int reputationAfter = bob.getReputation();

        assertEquals(-1, reputationAfter - reputationBefore);
    }

    @Test
    public void answererReputationIncreasesBy15WhenAnswerAccepted() throws Exception {
        int reputationBefore = bob.getReputation();
        alice.acceptAnswer(answer);

        int reputationAfter = bob.getReputation();

        assertEquals(15, reputationAfter - reputationBefore);
    }

    @Test
    public void authorUnableToUpvoteOwnPost() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        alice.upVote(question);
    }

    @Test
    public void nonQuestionerCannotAcceptAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only alice can accept this answer as it is their question");

        charles.acceptAnswer(answer);
    }
}