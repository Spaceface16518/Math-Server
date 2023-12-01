import java.io.*; 
import java.net.*;
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

        try (Socket clientSocket = new Socket("127.0.0.1", 6789))
        {
			DataOutputStream outToServer =  new DataOutputStream(clientSocket.getOutputStream()); 
			
			// Create new instance of output stream
			MessageOutputStream out = new MessageOutputStream(outToServer);
			   
			DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
			        
			@SuppressWarnings("resource")
			MessageInputStream in = new MessageInputStream(inFromServer);
			
			// Create new instance of output stream
			//MessageInputStream in = new MessageInputStream(inFromServer);
			
			// Variables
			int numCalcs = 0;
			int name = 0;
			int looping = 0;
			while (looping == 0)
			{
				// Check if client has name (client has just started)
				if (name == 0)
				{
					System.out.println("Please enter a name for the client.");
					
					while(name == 0)
					{
						// Read in sentence from user
			            sentence = inFromUser.readLine();
			            int senLen = sentence.length();
			            
			            // Check if sentence is empty
			            if(senLen == 0)
			            {
			            	System.out.println("Your client name is empty.  Please enter a new client name.");
			            } //end if statement
			            else
			            {
			            	// Create new message (Connection establishing message)
			    			ConnectionMessage conn = new ConnectionMessage(sentence);
			    			// Send message to server
			    			out.write(conn);
			    			out.flush();
			    			name = 1;
			            } // end else statement
			            
					} // end while statement
					
				} // end if statement
				else // Client established
				{
					// Read in sentence from user
			        sentence = inFromUser.readLine();
			        int senLen = sentence.length();
			        
			        if (senLen == 0)
			        {
			        	if (numCalcs < 3)
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
			                looping = 1; // set looping to 1 to break out of loop

			            } //end else statement
			        	
			        } //end if statement
			        else
			        {
			        	// Check if string only holds acceptable characters
			        	if (sentence.matches(" +-*/[0-9]"))
			        	{
			        		// If the amount of acceptable characters is the same as the sentence length
			                // I.e. there are no invalid characters within the sentence...
			        		numCalcs++;
			        	} //end if statement
			            else
			            {
			                System.out.println("You have entered an invalid expression.  Please try again.");
			                System.out.println("Note: Acceptable operators include +, -, /, and *");
			            } //end else statement
					
			        } //end else statement
				
				
				} // end while loop

			// Send calculation to server
			CalculationMessage calc = new CalculationMessage(sentence);
			out.write(calc);
			out.flush();

			// Print to screen server's message
			modifiedSentence = in.readMessage();

			System.out.println("From Server: " + modifiedSentence);
			
			} //end for loop
		} //end try
    } //end function main()
} //end class
