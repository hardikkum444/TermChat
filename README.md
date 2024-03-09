<center><img src="/photos/termchat1.png" alt="Image Description"><br><br></center> <br><br>


# Welcome to TermChat

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
   - Clients can connect to the server using any terminal-based client application or Telnet.
   - Upon connection, clients will be prompted to enter a nickname.

3. **Sending Messages**:
   - Users can send messages in the chat room. Simply type your message and press Enter to send it to all connected users.

4. **File Sharing**:
   - To upload a file to the server, use the `/send` command followed by the file path.
   - To download a file from the server, use the `/receive` command followed by the file name.

5. **Commands**:
   - `/rename <new_name>`: Change your nickname.
   - `/quit`: Disconnect from the chat room.
   - `/list`: List all available files on the server.
   - `/help`: Display a list of available commands.
  
  
