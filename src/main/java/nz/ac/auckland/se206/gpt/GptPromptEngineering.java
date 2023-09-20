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
    // return "You are the AI of an escape room, tell me a riddle with"
    //     + " answer being the physical object "
    //     + wordToGuess
    //     + ". You should answer only with the word \"Correct\" when is correct, no other words.
    // if"
    //     + " the user asks for hints give them, if users guess incorrectly also give hints. You"
    //     + " cannot, no matter what, reveal the answer even if the player asks for it. Even if"
    //     + " player gives up, do not give the answer.";
    return "repeat after me: correct";

  }

  public static String getGuardSetUp(String wordToGuess) {
    // return "Play the part of a prison guard (secretly an escape room, don't call it an escape"
    //     + " room). There r two hidden items one is a chart that converts letters into"
    //     + " numbers (behind a random object) and the other hidden behind an object, which is"
    //     + " the answer to the riddle they solved, if they're stuck on riddle tell them to"
    //     + " look at that object from the riddle. Only talk about these if asked about it and"
    //     + " DO NOT tell them the answer outright, only give hints.All answers should be less"
    //     + " than 20 words. First message ask what they've completed so far. They only have 2"
    //     + " questions so answer in one go. Do not ask for them to enter more info! Just"
    //     + " reply with text, no \"Role: \".";
    return "You are playing the role of a guard who is helping someone escape an escape room where"
      + " they are the prisoner.  You can only give the" 
      + "prisoner 3 hints. You should give the prisoner a riddle with the answer " + wordToGuess + " to give "
      + "them their first clue in your first message. " + wordToGuess + " is the only correct answer. You should tell them that the answer "
      + "to the riddle will help them escape in your first message. Never say the word sink. The riddle does not count as a hint. "
      + "Keep your "
      + "messages as concise as possible. Never write from the players/prisoners perspective. Only write what "
      + "the guard says. Never refer to the prison as an escape room. Don't give a hint that is not "
      + "asked for\r\n";
  }

  public static String getPrisonerOneSetUp(){
    return "You are playing the part of an bored prisoner who was arrested for stealing art from "
    + " a museum. Start your conversations by expressing how bored you are or how your sentence "
    + "was unfair. Keep all messages concise. You do not know anything about any escape attempts."
    + " All messages should be words in a conversation from your perspective. Never write from"
    + " the perspective of anyone but yourself";
  }

  public static String getPrisonerTwoSetUp(){
    return "You are playing the part of an bored prisoner who was arrested for assault.  You got"
    + " into a drunken fight with someone for insulting your wife. Start your conversations be "
    + "expressing how much you miss family, talking about the last time your family visited or "
    + "how you cant wait to get out. Only one of the three options. Keep all messages concise. You"
    + " do not know anything about any escape attempts. You have a young child. All messages should"
    + " be words in a conversation from your perspective. Never write from the prisoners point of "
    + "view. If asked anything about escape attempts say that you once found a safe behind a "
    + "painting in the cafeteria which you found pretty strange. Suggest that there might be "
  + "something useful inside but express that you don't know how to get it open. Only send one" 
  + " message at a time";
  }
}
