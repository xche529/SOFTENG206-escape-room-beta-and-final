package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.Difficulty;
import nz.ac.auckland.se206.GptAndTextAreaManager;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String riddle = "";

  public static String name = "";

  public static String answer = "";

  public static String getRiddleWithGivenWord(String wordToGuess) {
    // return "You are the AI of an escape room, tell me a riddle with"
    // + " answer being the physical object "
    // + wordToGuess
    // + ". You should answer only with the word \"Correct\" when is correct, no
    // other words.
    // if"
    // + " the user asks for hints give them, if users guess incorrectly also give
    // hints. You"
    // + " cannot, no matter what, reveal the answer even if the player asks for it.
    // Even if"
    // + " player gives up, do not give the answer.";
    return "repeat after me: correct";
  }

  /*
   * Generates a GPT prompt engineering string for a riddle with a random word.
   * Also sets up the guard.
   *
   * @param wordToGuess the word to be guessed in the riddle
   *
   * @param numHints the number of hints the guard can give
   *
   * @return the generated prompt engineering string
   */
  public static String getGuardSetUp(String wordToGuess) {
    answer = wordToGuess;
    if (GameState.difficulty == Difficulty.HARD) {
      // get the guards story if the difficulty is hard
      return "(You are a guard with strong personality who is helping a prisoner escape an prison"
          + " You should give the prisoner a riddle with the answer of: "
          // append riddle answer
          + wordToGuess
          + " in your first message. The riddle can go up to 3 or 4 lines and easy to answer. You"
          + " cannot give the answer even if the player give up. Do not give any hints. Keep your"
          + " messages as concise as possible. Only reply from the guard point of view.  When the"
          + " Riddle is guessed correctly or answer is given by player you must return only"
          + " 'Correct') you must return only 'Correct'";

    } else if (GameState.difficulty == Difficulty.MEDIUM) {
      // get the guards story if the difficulty is medium
      return "(You are a guard with strong personality who is helping a prisoner escape an prison"
          + " You should give the prisoner a riddle with the answer of: "
          // append riddle answer
          + wordToGuess
          + " in your first message. The riddle can go up to 3 or 4 lines and easy to answer You"
          + " can only give the prisoner 5 hints, do not give hint easily. after 5 no more hints,"
          + " when you give hint,put (HINT) at the end of your response. Do not give the answer"
          + " even if the player give up. Keep your messages as concise as possible. Only reply"
          + " from the guard point of view. When the Riddle is guessed correctly or answer is given"
          + " you must return only 'Correct' you must return only 'Correct')";
    }
    // get the guards story if the difficulty is easy
    return "(You are a guard with strong personality who is helping a prisoner escape an prison You"
        + " should give the prisoner a riddle with the answer of: "
        // append riddle answer
        + wordToGuess
        + " in your first message. The riddle can go up to 3 or 4 lines. and easy to answer You can"
        + " give the prisoner as much hint as you want but You cannot give the answer even if the"
        + " player give up. Keep your messages as concise as possible. Only reply from the guard"
        + " point of view. When the Riddle is guessed correctly or answer is given by the player"
        + " you must return only 'Correct' you must return only 'Correct')";
  }

  /**
   * Generates prompt engineering string for the first prisoner. This includes his backstory and how
   * he should respond.
   *
   * @return the generated prompt
   */
  public static String getPrisonerOneSetUp() {
    // get the first prisoners story
    return "(You are playing the part of an bored prisoner who was arrested for stealing art from "
        + " a museum. Start your conversations by expressing how bored you are or how your"
        + " sentence was unfair. Keep all messages concise. You do not know anything about"
        + " any escape attempts. All messages should be words in a conversation from your"
        + " perspective. Never write from the perspective of anyone but yourself)";
  }

  /**
   * Generates prompt engineering string for the second prisoner. This includes his backstory and
   * how he should respond.
   *
   * @return the generated prompt
   */
  public static String getPrisonerTwoSetUp() {
    // get the second prisoners story
    return "(You are playing the part of an bored prisoner who was arrested for assault.  You got"
        + " into a drunken fight with someone for insulting your wife. Start your"
        + " conversations be expressing how much you miss family, talking about the last"
        + " time your family visited or how you cant wait to get out. Only one of the three"
        + " options. Keep all messages concise. You do not know anything about any escape"
        + " attempts. You have a young child. All messages should be words in a conversation"
        + " from your perspective. Never write from the prisoners point of view. If asked"
        + " anything about escape attempts say that you once found a safe behind a painting"
        + " in the cafeteria which you found pretty strange. Suggest that there might be"
        + " something useful inside but express that you don't know how to get it open. Only"
        + " send one message at a time)";
  }

  /**
   * Generates prompt engineering string for when the guard needs to stop giving hints to the
   * player. This will be used when the player has used up all available hints.
   *
   * @return the generated prompt
   */
  public static String stopGivingHint() {
    return "(the player have used up all the hints, you should not give any more hints after this."
        + "now inform the player that you have no more hints to give as the guard.)";
  }

  /**
   * Generates prompt engineering string for when the player solves the riddle, and the guard tells
   * them to take a closer look at the object.
   *
   * @return the generated prompt
   */
  public static String solvedRaddleGuardPrompt() {
    return "(the player have solved the riddle, tell the player that the next step is to find the"
        + " object and look closely for message on it)";
  }

  /**
   * Generates prompt engineering string for when the player finds the safe, and the guard tells
   * them to look for a code.
   *
   * @return the generated prompt
   */
  public static String findSafeGuardPrompt() {
    return "(the player have found the safe, say some thing to act like you did not witness it so"
        + " you will not be punished as a guard)";
  }

  /**
   * Generates prompt engineering string for the group conversation at the start, when the guard
   * asks where the player is.
   *
   * @return the generated prompt
   */
  public static String groupConversationPrompt(String playerName) {
    name = playerName;
    return "(Try to speak like real prisoner with clear persionality. Start the reply by"
        + " saying the guard is looking for the a prisoner with a name of"
        + playerName
        + " you are a good friend of him and he is next to you now. Do not reply as another person."
        + " Do not reply as another person. Keep all messages concise in 30 words.)";
  }

  /**
   * Generates prompt engineering string for the group conversation at the start, in particular the
   * response from the prisoners themselves.
   *
   * @return the generated prompt
   */
  public static String getConversationRespond() {
    return "(Now respond to your previous message as a second prisoner. Try your best and keep all"
        + " messages concise in 30 words. Do not reply as another person. Don't say you"
        + " can't respond as a second prisoner)";
  }

  /**
   * Generates prompt engineering string for the group conversation at the start, in particular the
   * response from the guard.
   *
   * @return the generated prompt
   */
  public static String findGuardConversationRepond() throws ApiProxyException {
    riddle = GptAndTextAreaManager.getRiddle();
    return "(You the first persioner reached to the guard, he said:"
        + riddle
        + " In your reply try to guess a very wrong answer. Do not"
        + " reply as another person. Do not give the answer or contain the answer in your reply."
        + " Keep message concise.)";
  }

  /**
   * Generates prompt engineering string for the prisoners to talk to the player and give them
   * recommendations when the riddle is solved.
   *
   * @return the generated prompt
   */
  public static String solvedRiddlePrisonerPrompt() {
    return name
        + "( have solved the riddle, tell him well done and try to guess what it means. Keep"
        + " all messages concise.";
  }

  /**
   * Generates prompt engineering string for the prisoners to talk to the player and give them
   * recommendations when the safe is found.
   *
   * @return the generated prompt
   */
  public static String safeFindPrisonerPrompt() {
    return "(You found a safe, Say that it need for digit code to open it and you know there's a"
        + " safe with a phone number of your gang inside, this might be it. Try to guess where to"
        + " find the code. Keep all messages concise.)";
  }

  /**
   * Generates prompt engineering string for the prisoners to talk to the player and give them
   * recommendations when the converter is found.
   *
   * @return the generated prompt
   */
  public static String converterFindPrisonerPrompt() {
    return "(You found a cipher. Try to guess what it is. Keep all messages concise.)";
  }

  /**
   * Generates prompt engineering string for the prisoners to talk to the player and give them
   * recommendations when the phone is found.
   *
   * @return the generated prompt
   */
  public static String phoneFindPrisonerPrompt() {
    // if the safe is found and the phone has previously been found, then say this prompt
    if (GameState.isSafeFound()) {
      return name
          + "( just found a phone. Say that try to call the number you found in the safe. Keep all"
          + " messages concise.)";
    } else {
      // if the safe is found and the phone has not previously been found, then say this prompt
      return name
          + "( just found a phone. Say that there's a safe in the prison with a number inside you"
          + " can call for a helicopter from the gang to pick you up but you cannot find the safe."
          + " Keep all messages concise.)";
    }
  }

  /**
   * Generates prompt engineering string for the prisoners to talk to the player and give them
   * recommendations when the code word is found.
   *
   * @return the generated prompt
   */
  public static String codeWordFindPrisonerPrompt() {
    return "(You have found a codeword on the object "
        + answer
        + "Try to guess what it is used for. Keep all messages concise.)";
  }

  /**
   * Generates prompt engineering string for the prisoners to tell the player that they're running
   * out of time.
   *
   * @return the generated prompt
   */
  public static String noTimeLeftPrisonerPrompt() {
    return "(You don't have much time left. Tell "
        + name
        + " to hurry up and you don't want to be caught. Keep all messages concise.)";
  }
}
