Description
===========

Intelligent agents travel through the Internet collecting information.
The Mobile Information Agent Inc. (MIA) has stations around the globe. Each station has a server that
hosts documents to be shared with other MIA stations. A MIA officer can send an agent to all stations
and gather the documents' file path that are relevant to his/her query, so he/she can later fetch them.

Agent Server
------------

Each station has at least one ``Agent Server`` that has access to the inter-agency network as well as
to the filesystem directory with the documents to be shared. All Agent Servers have port 9090 listening
for management commands and any port listening for incoming Intelligent Agents.

An Agent Server should bootstrap from another existing server in the network and through an epidemic-style
dissemination, they will all discover each other and create an unstructured overlay. An Agent will travel
through this overlay to all Agent Servers and gather information.

Agent
-----

An officer at a MIA station can issue an Agent to travel to all MIA offices, scan all shared documents
and return a list with files that met the query. The query operates on files' metadata: title, author,
date and keywords and currently supports only pdf files but can easily be extended.

Client
------

A MIA officer can interact with the system through the client program. He/She can create a new agent with
specific query parameters and send it to the network. While the agent is travelling he/she can monitor its status,
its location and he/she can abord the agent and instruct it to return home. Upon completion of its task, the agent will
return home and compile a report in a file about the result.

A user can easily create a new custom query by implementing the class ``gr.kzps.id2212.project.client.query.QueryPlan``
The compiled class file of the above query plan will be passed as argument at the creation of the agent. An example
of a query plan is at ``userplan.UserQueryPlan.java``

Execution
=========

Every MIA station should run an Agent Server. The Base Service should listen to a well-known port (ideally the same for
all stations). The Agent Service port could be any port. An example of starting a Server Agent: 
``java -cp target/InfoSearch-XXX.jar -i someID -s a_search_dir/ -ap 6060 -bp 9090``. This will start the first node of the MIA
stations overlay with some ID, the directory of the shared documents, listening on port 9090 for the Base Service and on
port 6060 for the Agent Service. The rest of the nodes should be instructed to bootstrap from that node, so they
could be started as: ``java -cp target/InfoSearch-XXX.jar -i anotherID -s my_search_dir/ -bp 9090 -ap 5859 -bsn bootstrap_ip -bsp 9090``. Similarly, that node will be given some identifier, specify the shared directory, the Base port is
9090, the Service port is 5859, the IP of the bootstrap node and the Base Service port of the latter.

A user can create his/her own query for the agent as long as it implements the *import gr.kzps.id2212.project.client.query.QueryPlan* interface. An example can be found at *userplan.UserQueryPlan*. In order for
the user to compile the query plan, execute: ``javac -classpath target/original-InfoSearch-XXX.jar userquery/SomeQuery.java``

Finally, an agent can be sent to traverse MIA nodes by the client CLI. The client CLI in \*nix can be started by executing
the script *client_cli.sh* and specifing the classpath for the user defined query, ex: ``./client_cli.sh .`` if the query
class is in the same level as the script. Try ``help`` for the client tool commands. The result of the spawned agents will
be written in *results/* directory.

Have fun!
