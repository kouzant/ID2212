#!/bin/sh

java -cp $1 gr.kzps.id2212.project.agentserver.ServerExecEnv -i server3 \
-s NodeD/ -bp 9393 -ap 6363 -bsn localhost -bsp 9191
