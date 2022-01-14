package bot;

import net.dv8tion.jda.api.EmbedBuilder;

public interface MessageListener {
    public static final int TOO_MANY_ARGUMENTS = 0;


    default boolean isTooManyArguments(String[] partsOfCommand){ // 명령어의 인자가 두개 이상일경우 거부함
        if(partsOfCommand.length > 2)
            return true;
        return false;
    }

    default void setErrorMessage(EmbedBuilder eb, int index) { // 0 : TooManyArguments,
        switch (index) {
            case 0 -> {
                eb.setTitle("경고!");
                eb.addField("Too many arguments","너무 많은 인자를 입력하였습니다.",false);
            }

        }
    }

}
