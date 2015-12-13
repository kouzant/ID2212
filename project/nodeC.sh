#!/bin/sh

java -cp $1 gr.kzps.id2212.project.agentserver.ServerExecEnv -i server2 \
-s NodeC/ -bp 9292 -ap 6262 -bsn localhost -bsp 9191
