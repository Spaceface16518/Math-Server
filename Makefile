JAVAC=javac
JAVA=java

CLASSPATH=src/
BUILD_DIR=out/

all: server client

run_server:
	$(JAVA) -cp $(BUILD_DIR) Server

run_client:
	$(JAVA) -cp $(BUILD_DIR) Client

server:
	$(JAVAC) -d $(BUILD_DIR) -cp $(CLASSPATH) src/Server.java

client:
	$(JAVAC) -d $(BUILD_DIR) -cp $(CLASSPATH) src/Client.java

clean:
	$(RM) -rf $(BUILD_DIR)
