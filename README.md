# chat
Chat. In Java. Enjoy.

I'm not entirely sure how to compile this one (the client, at least) from the command line; you'll have to use IntelliJ for a simple compile.
Otherwise, I'm afraid you're on your own for how to compile this from just the command line. Apologies.

Once you've compiled it in IntelliJ, you can run the server from the command line by `cd`ing into the `chat/out/production/chat` folder and running:
```
java server/Server
```
You can run a client by similarly `cd`ing into `chat/out/production/chat`, but this time running:
```
java server/Client
```

The server is preconfigured to use the IP address of the machine it's running on, on port 8080. To change this, you'll have to mess around in the source code, particularly `chat/src/server/Server.java`.

The client, on the other hand, will prompt you to enter the IP address and port of a known server.

The client will also prompt you to enter in a username and password, but the server does not verify these values. It only uses the username as a display name for the duration of the client's connection to the server. You'll again have to muck about in the source to change this. You can add verification features in `chat/src/server/Server.java`. Maybe you might add a database to verify and hash the password; that's up to you.

I hope you enjoy this project. As always, if something goes catastrophically wrong, please don't sue me. Thanks. Cheers! I hope you enjoy.

Here are some example photos of what the client looks like:

![The connection page. You enter in the server's IP and port here first.](imgs/client_connect.png)

The connection page. You enter in the server's IP and port here first.

![The login page. You enter a username (and superfluous) password here.](imgs/client_login.png)

The login page. You enter a username (and superfluous) password here.

![The chat box. You chat here after connecting to a server and logging in.](imgs/client_chat.png)

The chat box. You chat here after connecting to a server and logging in.

Finally, here are some example photos of the server logging actions by clients:
![The server logs who (IP and username) logs in and what they send.](imgs/server_logs.png)
