# Bot that assigns roles with the use of reactions
import discord
import asyncio
import random
from discord.ext.commands import Bot
from discordToken import getToken

# Token hidden behind another function
TOKEN = getToken()

client = Bot("..")

@client.event
async def on_ready():
    print('Logged in as')
    print(client.user.name)
    print(client.user.id)
    print('------')
    await client.change_presence(game=discord.Game(name='Use ..help to start'))


@client.command(name="clear", description="Deletes the previous x messages from past 14 days in this channel. Only Admins and Moderators are able to use this", brief="Deletes x messages", pass_context=True)
async def clear(ctx, number = 0):
    userRoles = ctx.message.author.roles
    adminRole = (discord.utils.get(ctx.message.server.roles, name="Admin"))
    if adminRole.id in [y.id for y in userRoles]:
        print("Valid user clear: {} items".format(number))
        mgs = []  # Empty list to put all the messages in the log
        
        # Converting the amount of messages to delete to an integer
        # Find proper way to handle errors with discord.py 
        try:
            number = int(number)
        except ValueError:
            await client.say('Please give a valid number that is 1 or greater')
            return

        if number <= 0:
            await client.say('Please give a number that is 1 or greater')
            return
        else:
            # Delete the originating message
            number += 1

            async for x in client.logs_from(ctx.message.channel, limit=min(100, number)):
                mgs.append(x)
            await client.delete_messages(mgs)
            msg = await client.send_message(ctx.message.channel, 'Done deleting previous {} message(s)'.format((number - 1)))
            await asyncio.sleep(1.5)
            await client.delete_message(msg)

@client.command(name="roll", description="Takes a number input and rolls a die from 1 to that number, x times", brief="roll a y sided die x times", aliases=["die", "dice"], pass_context=True)
async def roll(ctx, sides=20, dice=1):
    try:
        sides = int(sides)
    except:
        await client.send_message(ctx.message.channel, 'Please submit a proper number to roll, in the format of ..roll x for 1dx or ..roll x y to roll ydx.')

    if sides < 2:
        await client.send_message(ctx.message.channel, 'Your number of sides: {}, is invalid, use a number 2 or greater.'.format(sides))
    else:
        try:
            dice = int(dice)
        except:
            await client.send_message(ctx.message.channel, "Please use a valid number of dice")
        
        if dice < 1:
            dice = 1
        
        await client.send_message(ctx.message.channel, "Rolling {}d{}".format(dice, sides))
        strOut = "You rolled: "
        for i in range(dice):
            roll = random.randrange(0, sides) + 1
            if(i > 0):
                strOut += ", {}".format(roll)
            else:
                strOut += "{}".format(roll)

        await client.send_message(ctx.message.channel, strOut)
    
# When adding a reaction, it adds a role
# Add similar function but to remove role when reaction removed
# Figure out how to do with an older message (From before the bot was live) SEE JAVA BOT, NO LONGER USING THIS IN PYTHON
'''
@client.event
async def on_reaction_add(reaction, user):

    roleChannelId = '469531044887003148'
    print("reaction seen")

    if reaction.message.channel.id != roleChannelId:
        print("wrong channel")
        return  # So it only happens in the specified channel

    # Get the specific ID for the message, should be able to check older messages
    # Add a hardcoded if statement with specific ID for the message, to check its reactions?

    print(reaction.message.id)

    if str(reaction.emoji) == "<:rocketleague:469532328411136030>":
        print("added role")
        await client.add_roles(user, (discord.utils.get(reaction.message.server.roles, name="Rocket League")))
    
    if str(reaction.emoji) == "<:csgo:469532328427913216>":
        print("added role")
        await client.add_roles(user, (discord.utils.get(reaction.message.server.roles, name="CS:GO")))

    if str(reaction.emoji) == "<:league:469532327953825812>":
        print("added role")
        await client.add_roles(user, (discord.utils.get(reaction.message.server.roles, name="League of Legends")))

    if str(reaction.emoji) == "<:pubg:469882172015116359>":
        print("added role")
        await client.add_roles(user, (discord.utils.get(reaction.message.server.roles, name="PUBG")))

    if str(reaction.emoji) == "<:runescape:469882172791062528>":
        print("added role")
        await client.add_roles(user, (discord.utils.get(reaction.message.server.roles, name="Runescape")))


@client.event
async def on_reaction_remove(reaction, user):

    roleChannelId = '469531044887003148'
    print("reaction seen")

    if reaction.message.channel.id != roleChannelId:
        print("wrong channel")
        return  # So it only happens in the specified channel

    # Get the specific ID for the message, should be able to check older messages
    # Add a hardcoded if statement with specific ID for the message, to check its reactions?

    print(reaction.message.id)

    if str(reaction.emoji) == "<:rocketleague:469532328411136030>":
        print("removed role")
        await client.remove_roles(user, (discord.utils.get(reaction.message.server.roles, name="Rocket League")))

    if str(reaction.emoji) == "<:csgo:469532328427913216>":
        print("removed role")
        await client.remove_roles(user, (discord.utils.get(reaction.message.server.roles, name="CS:GO")))

    if str(reaction.emoji) == "<:league:469532327953825812>":
        print("removed role")
        await client.remove_roles(user, (discord.utils.get(reaction.message.server.roles, name="League of Legends")))

    if str(reaction.emoji) == "<:pubg:469882172015116359>":
        print("removed role")
        await client.remove_roles(user, (discord.utils.get(reaction.message.server.roles, name="PUBG")))

    if str(reaction.emoji) == "<:runescape:469882172791062528>":
        print("removed role")
        await client.remove_roles(user, (discord.utils.get(reaction.message.server.roles, name="Runescape")))
'''
client.run(TOKEN)
