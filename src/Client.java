import java.io.*; 
import java.net.*;
import java.util.*;

import message.ConnectionMessage;
import message.Message;
import message.CalculationMessage;
import message.TerminationMessage;
import message.MessageInputStream;
import message.MessageOutputStream;



class TCPClient { 

    public static void main(String argv[]) throws Exception 
    { 
        String sentence = null; 
        Message modifiedSentence; 

        BufferedReader inFromUser = 
          new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("127.0.0.1", 6789); 

        DataOutputStream outToServer =  new DataOutputStream(clientSocket.getOutputStream()); 
        
        // Create new instance of output stream
        MessageOutputStream out = new MessageOutputStream(outToServer);
        
        
        DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
                
        MessageInputStream in = new MessageInputStream(inFromServer);
        
        // Create new instance of output stream
        //MessageInputStream in = new MessageInputStream(inFromServer);
        
        // Variables
		String[] acceptable = new String[] {"+", "-", "/", "*","0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int accLen = acceptable.length;
        int errorFree = 0;
        int numCalcs = 0;

        while (errorFree == 0)
        {
            if (numCalcs == 0)
            {
            	System.out.println("Please enter a name for the client.");
            }

            // Read in sentence from user
            sentence = inFromUser.readLine();
            int senLen = sentence.length();
            int good = 0;

            if (numCalcs == 0 && senLen > 0)
            {
            	// Create new message (Connection establishing message)
                ConnectionMessage conn = new ConnectionMessage(sentence);
                
                // Send message to server
                out.write(conn);
                
            	
            }
            else if (senLen == 0) // If sentence is empty, client wants to close connection
            {
                if (numCalcs < 3) // Client must send at least 3 calculations before closing
                {
                    int remaining = 3 - numCalcs;
                    System.out.println("You have only sent " + numCalcs + " operations out of 3.");
                    System.out.println("Please send " + remaining + " more valid calculations before you try to close the client.");
                } //end if statement

                else //Client has sent at least 3 calculations... Close client
                {
                    // Empty message indicates client wants to close connection
                	TerminationMessage term = new TerminationMessage();
                	// Send message to server
                    out.write(term);
                    
                    // Close output server
                    out.close();

                } //end else statement

            } //end else if statement
            else 
            {
                for (int i = 0; i < accLen; i++)
                {
                    if (sentence.contains(acceptable[i]))
                    {
                        good++;
                    } //end if statement
                    
                } // end for loop
                // If the amount of acceptable characters is the same as the sentence length
                // I.e. there are no invalid characters within the sentence...
                if (good >= senLen)
                {
                    // Set errorFree to 1 in order to break out of while loop
                    errorFree = 1;
                    numCalcs++;

                } // end if statement
                else
                {
                    System.out.println("You have entered an invalid expression.  Please try again.");
                    System.out.println("Note: Acceptable operators include +, -, /, * and ^");

                } //end else

            } //end else statement
            
        } // end while loop

        // Send calculation to server
        CalculationMessage calc = new CalculationMessage(sentence);
        out.write(calc);

        // Print to screen server's message
        modifiedSentence = in.readMessage();

        System.out.println("From Server: " + modifiedSentence);
        
    }
} 
