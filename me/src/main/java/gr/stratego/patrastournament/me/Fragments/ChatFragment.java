package gr.stratego.patrastournament.me.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import gr.stratego.patrastournament.me.Utils.StringUtils;

public class ChatFragment extends BaseStrategoFragment implements RoomListener {

    private String channelID = "Ei17UUzRvYa0YmJA";
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

        int textColor = Color.BLACK;
        if (StrategoApplication.getAppSettings() != null
                && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getDarkTextColor())
                && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getPrimaryColor())) {
            textColor = Color.parseColor(StrategoApplication.getAppSettings().getDarkTextColor());
        }

        editText.setTextColor(textColor);

        MemberData data = null;

        if(StrategoApplication.getCurrentUser() != null){
            data = new MemberData(StrategoApplication.getCurrentUser().getDisplayName(), getRandomColor());
        } else {
            data = new MemberData("Unknown user", getRandomColor());
        }

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                mRoom = scaledrone.subscribe(roomName, ChatFragment.this);
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


//        Room room = scaledrone.subscribe(roomName, this, new SubscribeOptions(100)); // ask for 50 messages from the history
//        room.listenToHistoryEvents(new HistoryRoomListener() {
//            @Override
//            public void onHistoryMessage(Room room, com.scaledrone.lib.Message message) {
//                System.out.println("Scaledrone Received a message from the past " + message.getData().asText());
//                onMessage(room, message);
//            }
//        });
        mAdapter = new MessageAdapter(getContext());
        messagesView.setAdapter(mAdapter);
    }
    @Override
    public void onOpen(Room room) {
        System.out.println("Scaledrone onOpen "+room.getName());
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.out.println("Scaledrone onOpenFailure "+room.getName());

    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        System.out.println("Scaledrone onMessage "+room.getName());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
            final Message message = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
