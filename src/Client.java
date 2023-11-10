import java.io.*; 
import java.net.*; 
class TCPClient { 

    public static void main(String argv[]) throws Exception 
    { 
        String sentence; 
        String modifiedSentence; 

        BufferedReader inFromUser = 
          new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("127.0.0.1", 6789); 

        DataOutputStream outToServer = 
          new DataOutputStream(clientSocket.getOutputStream()); 
        
        BufferedReader inFromServer = 
                new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream())); 

                // Variables
                List<String> acceptable = new ArrayList<String>() // list of acceptable characters (operators and numbers)
                {{
                    add("+"); add("-"); add("/"); add("*"); add("^"); // Operators
                    add("0"); add("1"); add("2"); add("3"); add("4"); // Numbers
                    add("5"); add("6"); add("7"); add("8"); add("9");
                }};
                int accLen = acceptable.size();
                int errorFree = 0;
                int numCalcs = 0;

                while errorFree == 0
                {
                    if (numCalcs == 0)
                    {
                        println("Please enter a name for the client.");
                    }

                    // Read in sentence from user
                    sentence = inFromUser.readLine();
                    int senLen = sentence.length();
                    int good = 0;

                    if (numCalcs == 0 && senLen > 0)
                    {
                        // Send calculation to server
                        outToServer.writeBytes("CONN: " + sentence + '\n');
                    }
                    else if (senLen == 0) // If sentence is empty, client wants to close connection
                    {
                        if numCalcs < 3 // Client must send at least 3 calculations before closing
                        {
                            int remaining = 3 - numCalcs;
                            println("You have only sent " + numCalcs + " operations out of 3.");
                            println("Please send " + remaining + " more valid calculations before you try to close the client.");
                        } //end if statement

                        else //Client has sent at least 3 calculations... Close client
                        {
                            // Empty message indicates client wants to close connection
                            outToServer.writeBytes("TERM: " + sentence + "\n");
                            clientSocket.close(); 

                        } //end else statement

                    } //end else if statement
                    else 
                    {
                        for (int i = 0; i < accLen; i++)
                        {
                            if sentence.contains(acceptable[i])
                            {
                                good++;
                            } //end if statement
                            
                        } // end for loop
                        // If the amount of acceptable characters is the same as the sentence length
                        // I.e. there are no invalid characters within the sentence...
                        if good >= senLen
                        {
                            // Set errorFree to 1 in order to break out of while loop
                            errorFree = 1;
                            numCalcs++;

                        } // end if statement
                        else
                        {
                            println("You have entered an invalid expression.  Please try again.");
                            println("Note: Acceptable operators include +, -, /, * and ^");

                        } //end else

                    } //end else statement
                    
                } // end while loop

                // Send calculation to server
                outToServer.writeBytes("CALC: " + sentence + '\n'); 

                modifiedSentence = inFromServer.readLine(); 

                System.out.println("From Server: " + modifiedSentence); 
   
          }
      } 

        