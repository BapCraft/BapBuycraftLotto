#BapBuycraftLotto

This is a plugin made for the Bapcraft Minecraft server to manage the sever-
side implementation of their Buycraft lottery.

To add a player to the lottery, use the command `addbcticket <id>`, where
the UUID is the player's account ID in the format
`XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX`, *OR* it could be the player's
username.

For players to check the status of the lottery, they should use the command
`buycraftlottocheck`.  This returns the information on the current pot.

**Notice to Developers**

When working on any non-trivial features, ones that could take more than one
commit to add, create a new commit to work on it.  Name the branch following
the pattern of `feature/$name`, where $name is a very short (preferable <12
chars) description of the feature.  For example, If you're working on adding
rank advancement prizes, the feature branch would be named
`feature/rankprizes`.  Once you've made any changes, checkout the master
branch, and merge the feature branch you were working on into master.  It is
important that you **DO NOT USE FAST-WORWARD**.  While this is a nifty
feature in git, it can be confusing and it is difficult to continue adding to
the feature if changes need to be made later.  If you do have to make changes
to a feature after it is merged, just checkout the feature branch, edit the
files as you need, checkout master, then make another merge.  The warning
above still applies.

**Indentation**

I (Treyzania) use the 1TBS indentation style in Java, so please do the same in
this project such that everything matches.  Same goes for the line skipping
format.

In addition, git can get upset if you don't end a file on a newline, so try to
do that wherever possible.
