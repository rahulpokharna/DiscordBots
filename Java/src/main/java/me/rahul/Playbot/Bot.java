package me.rahul.Playbot;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Bot extends ListenerAdapter{
    public static void main(String[] arguments) throws Exception
    {
        // Hide this key
        JDA api = new JDABuilder(AccountType.BOT).setToken("TOKEN").buildBlocking();
        api.addEventListener(new Bot());
    }

    public static void callShutdown(JDA self){
        self.shutdown();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        if (content.equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        }

        if(event.getMessage().getContentRaw().equals("!serverinfo")){
            // Make it so checks for admin role: 455093735118733312
            System.out.println("Getting server info");
            event.getTextChannel().sendMessage(createServerInfoEmbed(event.getGuild())).queue();
        }

        if(event.getMessage().getContentRaw().equals("^quit")){
            System.out.println("Exit command seen");
            List<Role> roles = event.getMember().getRoles();
            boolean admin = false;
            System.out.println("Roles for user calling command printing:");
            for(Role role: roles){
                System.out.println(role.getName() + ": " + role.getId());
                if(role.getId().equals("455093735118733312")){
                    System.out.println("Admin user");
                    admin = true;
                    break;
                }
            }
            if(admin){

                System.out.println("Exiting program");
                // Execute the shutdown here
                event.getJDA().shutdown();
                System.exit(0);
                // JDA#shutdown()
            }
            else{
                // maybe print a message?
                return;
            }

        }
    }


    // FIGURE OUT HOW THIS WORKS

    /**
     * Gives a server's text channels and roles, and their corresponding ids.
     * @param guild The guild to get information of
     * @return MessageEmbed An embed with this information contained in it
     */
    public static MessageEmbed createServerInfoEmbed(Guild guild) {
        System.out.println("Generating");
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.green);
        builder.setTitle("Server Info for " + guild.getName());
        StringBuilder sb = new StringBuilder();
        for(TextChannel c : guild.getTextChannels()) {
            sb.append(c.getName().substring(0, 1).toUpperCase() + c.getName().substring(1) + " - " + c.getId() + "\n");
        }
        builder.addField("Text Channels", sb.toString(), false);

        sb = new StringBuilder();
        for(Role r : guild.getRoles()) {
            sb.append(r.getName() + " - " + r.getId() + "\n");
        }
        builder.addField("Roles", sb.toString(), false);
        System.out.println("Completed, returning now");
        return builder.build();
    }


    /**
     * Might be changed to be used in more channels, to searh and compare names of reactions to names of roles
     * Gives roles to the user based upon their reactions in the main channel
     * @param event The reaction event to get the information of
     */
    public void onMessageReactionAdd​(MessageReactionAddEvent event){
        String channelID = event.getChannel().getId();
        System.out.println(channelID);
        if(channelID.equals("469531044887003148")){
            String reactionID = event.getReactionEmote().getId();
            System.out.println(reactionID);
            Member user = event.getMember();
            GuildController guildController = new GuildController(event.getGuild());

            // Rocket League reaction
            if(reactionID.equals("469532328411136030")){
                // add the rocket league role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("455949598310137856")).queue();
            }

            // CSGO Reaction
            else if(reactionID.equals("469532328427913216")){
                // add the cs role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("456952915337150477")).queue();
            }

            // League of Legends reaction
            else if(reactionID.equals("469532327953825812")){
                // add the league role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("469536494416035840")).queue();
            }

            // PUBG Reaction
            else if(reactionID.equals("469882172015116359")){
                // add the pubg role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("456952831858180107")).queue();
            }

            // Runescape Reaction
            else if(reactionID.equals("469882172791062528")){
                // add the Runescape role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("462528586604085248")).queue();
            }
            // Overwatch Reaction
            else if(reactionID.equals("469532327907688458")){
                // add the Overwatch role
                guildController.addRolesToMember(user, event.getGuild().getRoleById("475462905832144896")).queue();
            }
        }
    }

    /**
     * Removes the roles of a user based upon them removing the reaction in the channel
     * @param event The reaction to get the information from
     */
    public void onMessageReactionRemove​(MessageReactionRemoveEvent event){
        String channelID = event.getChannel().getId();
        System.out.println(channelID);
        if(channelID.equals("469531044887003148")){
            String reactionID = event.getReactionEmote().getId();
            System.out.println(reactionID);
            Member user = event.getMember();
            GuildController guildController = new GuildController(event.getGuild());
            // Rocket League reaction
            if(reactionID.equals("469532328411136030")){
                // add the rocket league role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("455949598310137856")).queue();
            }

            // CSGO Reaction
            else if(reactionID.equals("469532328427913216")){
                // add the cs role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("456952915337150477")).queue();
            }

            // League of Legends reaction
            else if(reactionID.equals("469532327953825812")){
                // add the league role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("469536494416035840")).queue();
            }

            // PUBG Reaction
            else if(reactionID.equals("469882172015116359")){
                // add the pubg role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("456952831858180107")).queue();
            }

            // Runescape Reaction
            else if(reactionID.equals("469882172791062528")){
                // add the Runescape role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("462528586604085248")).queue();
            }

            // Overwatch Reaction
            else if(reactionID.equals("469532327907688458")){
                // add the Overwatch role
                guildController.removeRolesFromMember(user, event.getGuild().getRoleById("475462905832144896")).queue();
            }
        }
    }
}
