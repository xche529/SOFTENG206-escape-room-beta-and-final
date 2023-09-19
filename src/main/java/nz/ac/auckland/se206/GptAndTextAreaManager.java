package nz.ac.auckland.se206;
import java.util.List;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CafeteriaController;
import nz.ac.auckland.se206.controllers.OfficeController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

public class GptAndTextAreaManager {
    public static ChatCompletionRequest guardChatCompletionRequest;
    public static ChatCompletionRequest prisonerOneCompletionRequest;
    public static ChatCompletionRequest prisonerTwoCompletionRequest;

    // These fields are initialized in the initialize method of the controller
    public static RoomController roomController;
    public static CafeteriaController cafeteriaController;
    public static OfficeController officeController;

    public static Text roomTypePromptText;
    public static Text officeTypePromptText;
    public static Text cafeteriaTypePromptText;

    public static TextArea roomChatDisplayBoard;
    public static TextArea officeChatDisplayBoard;
    public static TextArea cafeteriaChatDisplayBoard;

    public static TextArea roomInputBox;
    public static TextArea officeInputBox;
    public static TextArea cafeteriaInputBox;

    public static TextArea roomObjectiveDisplayBoard;
    public static TextArea officeObjectiveDisplayBoard;
    public static TextArea cafeteriaObjectiveDisplayBoard;

    public static boolean isGptRunning = false;

    private static String getMessageHistory(ChatCompletionRequest chat) {
        String result = "";
        List<ChatMessage> messages = chat.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            result += messages.get(i).getRole() + ": " + chat.getMessages().get(i).getContent() + "\n\n";
        }
        return result;
    }
}
