package nz;

import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

public class GptAndTextAreaManager {
    public static ChatCompletionRequest guardChatCompletionRequest;
    public static ChatCompletionRequest prisonerOneCompletionRequest;
    public static ChatCompletionRequest prisonerTwoCompletionRequest;
    public static Text roomText;
    public static Text officeText;
    public static Text cafeteriaText;

    public static TextArea roomMessageDisplayArea;
    public static TextArea officeMessageDisplayArea;
    public static TextArea cafeteriaMessageDisplayArea;

    public static TextArea roomMessageEditArea;
    public static TextArea officeMessageEditArea;
    public static TextArea cafeteriaMessageEditArea;

    public static TextArea roomObjectivTextArea;
    public static TextArea officeObjectivTextArea;
    public static TextArea cafeteriaObjectivTextArea;

    public static boolean isGptRunning = false;
    
}
