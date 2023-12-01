JAVAC=javac

CLASSPATH=src/
BUILD_DIR=out/

all: server client

run_server:
	java -cp $(BUILD_DIR) Server

run_client:
	java -cp $(BUILD_DIR) Client

server:
	$(JAVAC) -d $(BUILD_DIR) -cp $(CLASSPATH) src/Server.java

client:
	$(JAVAC) -d $(BUILD_DIR) -cp $(CLASSPATH) src/Client.java

clean:
	rm -rf $(BUILD_DIR)
