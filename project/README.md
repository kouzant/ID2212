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
query plan is at ``userplan.UserQueryPlan.java``
