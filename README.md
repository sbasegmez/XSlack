# XSlack

### Slack Integration for XPages/Domino

This project is a pre-release code to integrate Slack and XPages/Domino environments. Initial version contains;

- Configuration bean,
- A simple SlashCommand processor for [XSnippets](http://openntf.org/xsnippets) Search (/xsnippets)

### How it works?

- The Slack sends the slash command into `command.xsp` XAgent.
- RequestHandler gets the request;
  - Populate SlashCommand and SlashResponse objects
  - Determines which CommandHandler will process the request.
  - Initiate CommandHandler.

### Try?

- Register [XPages Slack Community](https://xpages-slack.herokuapp.com/)
- Use `/xsnippets` command.

### How to use?

- Create NSF on your web server.
  - Anonymous should be Depositor with Public Read/Write access.
  - After first use, there will be a Configuration document with your server name.
  - When needed, use reload.xsp page to refresh configuration bean.
- Configure Slack
  - In slack point a new SlackCommand to `http://yourserver/path/to/slack.nsf/command.xsp`
  - The command should be POST

### Future?

We'll look into other scenarios and try to evolve this project into a mature state in the future.

Fork and Contribute :)

### Legal Stuff

(c) 2015 Serdar Başeğmez ( [Blog](http://lotusnotus.com/en) | [Twitter](http://twitter.com/sbasegmez) )


Licensed under Apache Licence 2.0.
