package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer being the physical object "
        + wordToGuess
        + ". You should answer only with the word \"Correct\" when is correct, no other words. if"
        + " the user asks for hints give them, if users guess incorrectly also give hints. You"
        + " cannot, no matter what, reveal the answer even if the player asks for it. Even if"
        + " player gives up, do not give the answer.";
  }

  public static String getGuardSetUp() {
    return "Play the part of a prison guard (secretly an escape room, don't call it an escape"
        + " room). There r two hidden items one is a chart that converts letters into"
        + " numbers (behind a random object) and the other hidden behind an object, which is"
        + " the answer to the riddle they solved, if they're stuck on riddle tell them to"
        + " look at that object from the riddle. Only talk about these if asked about it and"
        + " DO NOT tell them the answer outright, only give hints.All answers should be less"
        + " than 20 words. First message ask what they've completed so far. They only have 2"
        + " questions so answer in one go. Do not ask for them to enter more info! Just"
        + " reply with text, no \"Role: \".";
  }
}
