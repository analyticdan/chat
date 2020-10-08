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

I hope you enjoy this project. As always, if something goes catastrophically wrong, please don't sue me. Thanks. Cheers! I hope you enjoy.
