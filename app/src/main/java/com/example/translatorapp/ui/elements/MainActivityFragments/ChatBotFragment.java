package com.example.translatorapp.ui.elements.MainActivityFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.translatorapp.databinding.FragmentChatBotBinding;
import com.example.translatorapp.ui.elements.adapter.ChatBotAdapter;
import com.example.translatorapp.ui.elements.models.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBotFragment extends Fragment {

    FragmentChatBotBinding binding;

    private final String baseUrl = "https://api.openai.com/v1/chat/completions";

    ArrayList<Message> messageList;
    private ChatBotAdapter adapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public ChatBotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBotBinding.inflate(getLayoutInflater());
        messageList = new ArrayList<>();

        adapter = new ChatBotAdapter(messageList);
        binding.chatbotRecyclerview.setHasFixedSize(true);
        binding.chatbotRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatbotRecyclerview.setAdapter(adapter);

        binding.sendButton.setOnClickListener(view -> {
            String msg = binding.messageEditText.getText().toString().trim();
            if (!msg.isEmpty()) {
                addToChat(msg, Message.SENT_BY_ME);
                binding.messageEditText.setText("");
                try {
                    callApi(msg);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return binding.getRoot();
    }

    public void addToChat(String message, String sentBy) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                adapter.notifyDataSetChanged();
                binding.chatbotRecyclerview.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    public void callApi(String msg) throws JSONException {

        // Create the request JSON as per your provided example
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-3.5-turbo-0613");
        JSONArray messagesArray = new JSONArray();
        messagesArray.put(new JSONObject().put("role", "user").put("content", msg));
        jsonBody.put("messages", messagesArray);

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        // Replace 'YOUR_API_KEY' with your actual OpenAI API key
        Request request = new Request.Builder()
                .url(baseUrl)
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray choicesArray = jsonObject.getJSONArray("choices");
                        if (choicesArray.length() > 0) {
                            String result = choicesArray.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            addResponse(result.trim());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }

    public void addResponse(String response) {
        addToChat(response, Message.SENT_BY_BOT);
    }
}
