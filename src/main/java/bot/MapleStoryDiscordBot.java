package bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class MapleStoryDiscordBot extends ListenerAdapter implements MessageListener {
    private String token;
    private JDA jda;

    public MapleStoryDiscordBot(String token){
        setToken(token);
    }

    public final void buildBot(){ // do not override
        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(this)
                    .build();
            jda.setAutoReconnect(true);
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message msg = event.getMessage();

        if(user.isBot())
            return;

        EmbedBuilder eb = new EmbedBuilder();

        buildMessage(msg, eb);
        sendMessage(channel, eb);

    }

    private void buildMessage(Message msg, EmbedBuilder eb) {
        String command = msg.getContentRaw();

        if(command.charAt(0) == '$'){
            switch (command.substring(1,command.length())){
                case "도움말","도움","ㄷㅇㅁ","ㄷㅇ" -> {
                    eb.setTitle("도움말");
                    eb.addField("캐릭터 검색",
                            "닉네임을 입력하여 해당 캐릭터의 정보를 조회할 수 있습니다.\n" +
                                    "사용방법 : $캐릭터검색 <닉네임>",false);
                    eb.setFooter("제작자 후원 : 국민 752601-04-321191");
                }

                default -> {
                    String[] partsOfCommand = command.split(" ");
                    if(command.contains("캐릭터검색")){

                        if(isTooManyArguments(partsOfCommand)) {
                            setErrorMessage(eb,TOO_MANY_ARGUMENTS);
                            return;
                        }








                    }else if(command.contains("")){

                    }
                }
            }
        }

    }

    private void sendMessage(MessageChannel channel, EmbedBuilder eb) {
        if(!eb.isEmpty())
            channel.sendMessageEmbeds(eb.build()).queue();
    }

    private void setToken(String token) {
        this.token = token;
    }

}
