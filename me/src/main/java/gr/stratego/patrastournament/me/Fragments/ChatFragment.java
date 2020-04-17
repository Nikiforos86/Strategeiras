package gr.stratego.patrastournament.me.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.HistoryRoomListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.ObservableRoomListener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.scaledrone.lib.SubscribeOptions;

import java.util.ArrayList;
import java.util.Random;

import gr.stratego.patrastournament.me.Adapters.MessageAdapter;
import gr.stratego.patrastournament.me.Models.MemberData;
import gr.stratego.patrastournament.me.Models.Message;
import gr.stratego.patrastournament.me.R;
import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.GeneralUtils;
import gr.stratego.patrastournament.me.Utils.JsonUtils;
import gr.stratego.patrastournament.me.Utils.SharedPreferencesUtil;
import gr.stratego.patrastournament.me.Utils.StringUtils;
import timber.log.Timber;

public class ChatFragment extends BaseStrategoFragment implements RoomListener {

    private String channelID = "oqzR8mHtOuJeLb9b";
    private Room mRoom;
    private String roomName = "observable-Strategeiras";
    private EditText editText;
    private View sendButton;
    private ListView messagesView;
    private Scaledrone scaledrone;
    private MessageAdapter mAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void updateData(ArrayList<?> list) {

    }

    @Override
    public void updateUI() {

    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        messagesView = (ListView) view.findViewById(R.id.messages_view);
        editText = view.findViewById(R.id.editText);
        sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int textColor = Color.WHITE;

                if (StrategoApplication.getAppSettings() != null
                        && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getLightTextColor())) {
                    textColor = Color.parseColor(StrategoApplication.getAppSettings().getLightTextColor());
                }

                editText.setTextColor(textColor);

                scaledrone = new Scaledrone(channelID);
                scaledrone.connect(new Listener() {
                    @Override
                    public void onOpen() {
                        System.out.println("Scaledrone connection open");
                        // Since the MainActivity itself already implement RoomListener we can pass it as a target
//                        mRoom = scaledrone.subscribe(roomName, ChatFragment.this);
                        mRoom = scaledrone.subscribe(roomName, ChatFragment.this, new SubscribeOptions(50)); // ask for 50 messages from the history
                        mRoom.listenToHistoryEvents(new HistoryRoomListener() {
                            @Override
                            public void onHistoryMessage(Room room, com.scaledrone.lib.Message message) {
                                Timber.d("Scaledrone onHistoryMessage");
                                onMessage(room, message);
                            }
                        });
                    }

                    @Override
                    public void onOpenFailure(Exception ex) {
                        System.out.println("Scaledrone onOpenFailure");
                        System.err.println(ex);
                    }

                    @Override
                    public void onFailure(Exception ex) {
                        System.out.println("Scaledrone onFailure");
                        System.err.println(ex);
                    }

                    @Override
                    public void onClosed(String reason) {
                        System.out.println("Scaledrone onClosed");
                        System.err.println(reason);
                    }
                });
            }
        }, 2000);


        mAdapter = new MessageAdapter(getContext());
        messagesView.setAdapter(mAdapter);
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Scaledrone onOpen " + room.getName());
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.out.println("Scaledrone onOpenFailure " + room.getName());

    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        System.out.println("Scaledrone onMessage " + room.getName());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final Message message = (Message) JsonUtils.convertToDesiredObject(receivedMessage.getData().asText(), Message.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {
        String textMessage = editText.getText().toString();
        if (textMessage.length() > 0) {
            String username = "Unknown user";
            String userColor = SharedPreferencesUtil.loadSharedPreference(SharedPreferencesUtil.UserColor, SharedPreferencesUtil.UserColor);

            if (StrategoApplication.getCurrentUser() != null) {
                username = StrategoApplication.getCurrentUser().getDisplayName();
            }

            if(StringUtils.isNullOrEmpty(userColor)){
                userColor = getRandomColor();
            }

            Message sendingMessage = new Message();
            sendingMessage.setText(textMessage);
            sendingMessage.setUsername(username);
            sendingMessage.setColor(userColor);

            scaledrone.publish(roomName, JsonUtils.convertJsonObjectToString(sendingMessage));
            editText.getText().clear();
        }
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        String color = sb.toString().substring(0, 7);
        SharedPreferencesUtil.saveSharedPreference(color, SharedPreferencesUtil.UserColor, SharedPreferencesUtil.UserColor);
        return color;
    }
}
