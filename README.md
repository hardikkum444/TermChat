# Welcome to TermChat

<center><img src="/photos/termchat1.png" alt="Image Description"><br><br></center> <br><br>



# What is termchat?

TermChat is a terminal-based chatting server application written in Java. 
It utilizes Java socket programming to establish connections and facilitate communication between clients.

## Features

- **Terminal-Based**: TermChat operates entirely within the terminal environment, making it lightweight and easy to use.
- **Chatting**: Users can connect to the server and engage in real-time text-based conversations.
- **File Sharing**: TermChat supports sending and downloading files between users connected to the server.
- **Nicknaming**: Users can choose their display name upon connecting to the server.
- **Dynamic Port**: The server dynamically assigns a port for listening to incoming connections.

## How to Use

1. **Running the Server**:
   - Compile and run the `Server.java` file to start the server.
   - The server will listen for incoming connections on a dynamically assigned port (default port: 9999).

2. **Connecting to the Server**:
   - Clients can connect simpy by compling the Client.java file and running it!
   - **make sure that in the clinet file you add the appropriate IP (public) of the server!**
   - Upon connection, clients will be prompted to enter a nickname.

3. **Sending Messages**:
   - Users can send messages in the chat room. Simply type your message and press Enter to send it to all connected users.

4. **File Sharing**:
   - To upload a file to the server, use the `/send` command followed by the file path.
   - To download a file from the server, use the `/receive` command followed by the file name.

5. **Commands**:

   | Command   | Description                                  |
   |-----------|----------------------------------------------|
   | /list     | List all available files on the server.      |
   | /help     | If you're clueless                           |
   | /send     | `</send (fileName.extension)>` to upload     |
   | /receive  | `</receive (fileName.extension)>` to download|
   | /rename   | Change your chatname                         |
   | /quit     | To leave the chat room                       |


## Requirements

- Java Development Kit (JDK) (anything >=8 works like a charm)
- Terminal or Command Prompt

## Contributions

*1> There still are some bugs that are pending to be fixed in this hilariously unclean code* <br>
*2> That being contributions are welcome* <br>
*3> If you notice any bugs please note that the bug fixes are on their way* <br>
*4> Also the spelling of receive is wrong (recieve and not receive) (please use /recieve ... bug fixes are on their way!* <br>

## License

This project is licensed under the [MIT License](LICENSE).



  
  
